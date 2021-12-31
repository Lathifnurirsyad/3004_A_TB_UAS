import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

//class BarangMasuk
public class BarangMasuk extends Barang
{
    //koneksi database
    Connection conn;
    String link = "jdbc:mysql://localhost:3306/database_gudang";
    
    Scanner input = new Scanner(System.in);
    boolean barangTersedia=false, vendorTersedia=false;
    Integer stok, stokAwal, stokAkhir, harga;
    String nama_vendor, id_vendor;

    public void nama_barang()
    {
        System.out.print("    Nama barang\t: ");
        this.nama = input.nextLine();
    }

    public void id_barang()
    {
        System.out.print("    ID barang\t: ");
        this.id_barang = input.nextLine();
    }

    public void harga()
    {
        System.out.print("    Harga (Rp) \t: ");
        this.harga = input.nextInt();
        //input.nextLine();
    }

    public void stok()
    {
        System.out.print("    Stok barang\t: ");
        this.stok = input.nextInt();
        input.nextLine();
    }

    public void nama_vendor()
    {
        System.out.print("    Nama vendor\t: ");
        this.nama_vendor = input.nextLine();
    }

    public void id_vendor()
    {
        System.out.print("    ID vendor\t: ");
        this.id_vendor = input.nextLine();
    }

    public void cekID_barang() throws SQLException
    {
        //mengakses data database_gudang tabel barang
        conn = DriverManager.getConnection(link,"root","");
        Statement statement = conn.createStatement();
        String sql = "SELECT * FROM barang WHERE id_barang LIKE '"+this.id_barang+"'";
        ResultSet result = statement.executeQuery(sql); 

        if (result.next())
        {
            System.out.println("ID barang "+this.id_barang+" pernah diinputkan");
            this.stokAwal = result.getInt("stok");
            this.nama = result.getString("nama");
            barangTersedia = true;
        }
    }

    public void barangTersedia() throws SQLException
    {
        //mengakses data database_gudang tabel barang
        this.stokAkhir = this.stokAwal + this.stok;
        String sql = "SELECT * FROM barang WHERE id_barang = '"+this.id_barang+"'";
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(sql);

        if (result.next())
        {
            //update data database_gudang tabel barang
            System.out.println("ID barang "+this.id_barang+" berubah "+this.stokAwal+" jadi "+this.stokAkhir+" pcs");
            sql = "UPDATE barang SET stok = "+this.stokAkhir+" WHERE id_barang ='"+this.id_barang+"'";  
            
            if(statement.executeUpdate(sql) > 0)
            {
                System.out.println("Berhasil memperbaharui stok");
            }    
        }
    }

    public void vendorTersedia() throws SQLException
    {
        //input data ke database_gudang tabel barang_masuk
        conn = DriverManager.getConnection(link,"root","");
        String sql = "INSERT INTO barang_masuk (nama_barang, id_barang, harga, stok, nama_vendor, id_vendor) VALUES('"+this.nama+"', '"+this.id_barang+"', "+this.harga+", "+this.stok+", '"+this.nama_vendor+"', '"+this.id_vendor+"')";
        Statement statement = conn.createStatement();
        statement.execute(sql);
        //System.out.println("Berhasil input data");
    }

    public void cekID_vendor() throws SQLException
    {
        //mengakses data database_gudang tabel barang_masuk
        conn = DriverManager.getConnection(link,"root","");
        Statement statement = conn.createStatement();
        String sql = "SELECT * FROM barang_masuk WHERE id_vendor LIKE '"+this.id_vendor+"'";
        ResultSet result = statement.executeQuery(sql); 

        if (result.next())
        {
            System.out.println("ID vendor "+this.id_vendor+" pernah diinputkan");
            //menyimpan nilai dari data database_gudang tabel barang_masuk ke varible
            this.nama_vendor = result.getString("nama_vendor");
            vendorTersedia = true;
        }
    }

    public void buatBarangBaru() throws SQLException
    {
        Barang barang = new Barang(this.nama, this.id_barang);
        barang.methodKosong();

        conn = DriverManager.getConnection(link,"root","");
        Statement statement = conn.createStatement();
        //input data ke database_gudang tabel barang
        String sql = "INSERT INTO barang (nama, id_barang, harga, stok) VALUES ('"+this.nama+"', '"+this.id_barang+"', "+this.harga+", "+this.stok+")";
        statement.execute(sql);
        System.out.println("Berhasil input data");
    }
    
    public void buatVendorBaru() throws SQLException
    {
        Vendor vendor = new Vendor(this.nama_vendor, this.id_vendor);
        vendor.methodKosong();

        conn = DriverManager.getConnection(link,"root","");
        Statement statement = conn.createStatement();
        //input data ke database_gudang tabel barang_masuk
        String sql = "INSERT INTO barang_masuk (nama_barang, id_barang, harga, stok, nama_vendor, id_vendor) VALUES('"+this.nama+"', '"+this.id_barang+"', "+this.harga+", "+this.stok+", '"+this.nama_vendor+"', '"+this.id_vendor+"')";
        statement.execute(sql);
    }

    @Override
    public void save() throws SQLException 
    {
        //try
        try
        {
            nama_vendor();
            id_vendor();
            cekID_vendor();
            nama_barang();
            id_barang();
            harga();
            stok();
            cekID_barang();
            
            //percabangan if
            if (barangTersedia)
            {
                //percabangan if
                if (vendorTersedia)
                {
                    vendorTersedia();
                    barangTersedia();
                }
                else
                {
                    buatVendorBaru();
                    barangTersedia();
                }
            }
            else
            {
                //percabangan if
                if (vendorTersedia)
                {
                    vendorTersedia();
                    buatBarangBaru();
                }
                else
                {
                    buatVendorBaru();
                    buatBarangBaru();
                }
            }
        }
        //exception SQL
        catch(SQLException e)
        {
            System.err.println("Kesalahan dalam input data");
        }
        //excception input tidak sesuai dengan tipe data
        catch(InputMismatchException e)
        {
            System.out.println("Inputan data dengan benar");
        }
    }
}
