import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

//class BarangKeluar
public class BarangKeluar extends Barang
{
    Connection conn;
    String link = "jdbc:mysql://localhost:3306/database_gudang";
    
    Scanner input = new Scanner(System.in);
    String pj, id_pj, nama, id_barang;
    boolean barangTersedia=false, barangCukup=false, barangPas=false;
    Integer stok, stokKeluar, stokAwal, stokAkhir;

    
    public void nama_pj()
    {
        System.out.print("    Nama pj\t: ");
        this.pj = input.nextLine();
    }

    public void id_pj()
    {
        System.out.print("    ID pj\t: ");
        this.id_pj = input.nextLine();
    }

    public void nama_barang()
    {
        System.out.print("    Nama barang\t: ");
        this.nama = input.nextLine();
    }

    public void id_barang()
    {
        System.out.print("    ID barang\t : ");
        this.id_barang = input.nextLine();
    }

    public void barang_keluar()
    {
        System.out.print("    Jumlah stok keluar\t: ");
        this.stokKeluar = input.nextInt();
    }

    public void stok(Integer stok)
    {
        this.stokAwal = stok;
    }

    public void cekStok()
    {
        if (this.stokAwal > this.stokKeluar)
        {
            barangCukup = true;
        }
        else if (this.stokAwal.equals(this.stokKeluar))
        {
            barangPas = true;
        }
        else 
        {
            System.out.println("Jumlah stok di gudang tidak cukup");
        }
    }

    public void stokBerkurang() throws SQLException 
    {
        //logika matematika untuk memperoleh nilai dari stok akhir barang
        this.stokAkhir = this.stokAwal - this.stokKeluar;
        conn = DriverManager.getConnection(link,"root","");
        String sql = "SELECT * FROM barang WHERE id_barang='"+this.id_barang+"'";
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(sql);

        if (result.next())
        {
            sql = "UPDATE barang SET stok = "+this.stokAkhir+" WHERE id_barang ='"+this.id_barang+"'";
            if(statement.executeUpdate(sql) > 0)
            {
                 System.out.println("Berhasil memperbaharui stok (ID Barang "+this.id_barang+")");
            }
        }

    }

    public void stokHabis() throws SQLException
    {
        //conn = DriverManager.getConnection(link,"root","");
        String sql = "DELETE FROM barang WHERE id_barang = '"+this.id_barang+"' ";
        Statement statement = conn.createStatement();

        if(statement.executeUpdate(sql) > 0)
        {
            System.out.println("ID barang "+this.id_barang+" stoknya telah habis");
        }
    }

    public void isiTabelBarangKeluar() throws SQLException
    {
        conn = DriverManager.getConnection(link,"root","");
        Statement statement = conn.createStatement();
        String sql = "INSERT INTO barang_keluar (nama_barang, id_barang, jumlah, nama_pj, id_pj) VALUES ('"+this.nama+"', '"+this.id_barang+"', "+this.stokKeluar+", '"+this.pj+"', '"+this.id_pj+"')";
        statement.execute(sql);
    }

    @Override
    public void search() throws SQLException 
    {
        //nama_barang();
        id_barang();
        conn = DriverManager.getConnection(link,"root","");
        Statement statement = conn.createStatement();
        String sql = "SELECT * FROM barang WHERE id_barang LIKE '"+this.id_barang+"' ";
        ResultSet result = statement.executeQuery(sql); 

        if (result.next())
        {
            barangTersedia = true;
            this.nama = result.getString("nama");
            this.stok = result.getInt("stok");
            stok(result.getInt("stok"));
            System.out.println("Barang ada di gudang sebanyak "+this.stok+" pcs");
            
        }
    }

    @Override
    public void delete() throws SQLException 
    {
        try
        {
            view();
            System.out.println("Isi data berikut");
            nama_pj();
            id_pj();
            search();
            
            if (barangTersedia)
            {
                barang_keluar();
                cekStok();

                if (barangCukup)
                {
                    stokBerkurang();
                    isiTabelBarangKeluar();
                }
                else if(barangPas)
                {
                    stokHabis();
                    isiTabelBarangKeluar();
                }
            }
            else
            {
                System.out.println("Barang tidak tersedia di gudang");
            }
        }
        catch(SQLException e)
        {
            System.out.println("Gagal input data");
        }
        catch(InputMismatchException e)
        {
            System.out.println("Masukkan data yang benar");
        }
    }
}
