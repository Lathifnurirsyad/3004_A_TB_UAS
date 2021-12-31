import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

//class Barang
public class Barang implements Database //class Barang yang mengimplementasikan interface Database
{
    //koneksi database
    Connection conn;
    String link = "jdbc:mysql://localhost:3306/database_gudang";
    Scanner input = new Scanner(System.in);

    String nama, id_barang;
    Integer harga, stok;
    boolean kosongView = true;

    //constructor
    public Barang ()
    {

    }

    //constructor
    public Barang(Integer a)
    {
        System.out.println("Constructor class Barang parameter integer");
    }

    //constructor
    public Barang (String nama, String id_barang)
    {
        this.nama = nama;
        this.id_barang = id_barang;
        System.out.println("Barang baru telah disimpan \n"+this.nama+" dengan kode "+id_barang);
    }

    public void methodKosong()
    {

    }

    //method implementasi dari interface
    @Override
    public void view() throws SQLException 
    {
        //mengakses data yang berada di database database_gudang tabel barang
        String sql = "SELECT * FROM barang";
        conn = DriverManager.getConnection(link,"root","");
        Statement statement = conn.createStatement();
		ResultSet result = statement.executeQuery(sql);

        //percabangan while
        while (result.next())
        {
            kosongView = false;
            //block program untuk mengakses data di tabel barang dan mencetaknya di console
            System.out.print("\nNama\t  : ");
            System.out.println(result.getString("nama"));
            System.out.print("ID Barang : ");
            System.out.println(result.getString("id_barang"));
            System.out.print("Harga\t  : ");
            System.out.println(result.getInt("harga"));
            System.out.print("Stok\t  : ");
            System.out.println(result.getInt("stok"));
        }

        if (kosongView)
        {
            System.out.println("Data stok barang kosong");
        }
        statement.close();
    }

    //method implementasi dari interface
    @Override
    public void update() throws SQLException 
    {
        //try
        try
        {
            view();
            Integer pil;

            //percabangan if
            if (kosongView)
            {
                System.out.println("Data kosong tidak dapat mengubah data");
            }
            else
            {
                String text = "\n Ubah stok barang";
                System.out.println(text.toUpperCase());
                System.out.print("ID Barang barang hendak diubah : ");
                String ubah = input.nextLine();
    
                //mengakses data database database_gudang tabel barang
                String sql = "SELECT * FROM barang WHERE id_barang='"+ubah+"'";
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql);
    
                //percabangan  if
                if (result.next())
                {
                    System.out.println("Data yang hendak diubah\n1.Nama\n2.Harga\n3.Stok");
                    System.out.print("Pilihan (1/2/3) : ");
                    pil = input.nextInt();
                    input.nextLine();
            
                    //percabangan switch case
                    switch (pil)
                    {
                        case 1:
                            System.out.print("Nama ["+result.getString("nama")+"]\t: ");
                            String ganti1 = input.nextLine();
                            //update data  database database_gudang tabel barang
                            sql = "UPDATE barang SET nama = '"+ganti1+"' WHERE id_barang ='"+ubah+"'";
                            if(statement.executeUpdate(sql) > 0)
                            {
                                System.out.println("Berhasil memperbaharui nama (ID Barang "+ubah+")");
                            }
                        break;
            
                        case 2:
                            System.out.print("Harga ["+result.getInt("harga")+"]\t: ");
                            Integer ganti2 = input.nextInt();
                            //update data  database database_gudang tabel barang
                            sql = "UPDATE barang SET harga = "+ganti2+" WHERE id_barang ='"+ubah+"'";
                            input.nextLine();
                            if(statement.executeUpdate(sql) > 0)
                            {
                                System.out.println("Berhasil memperbaharui harga (ID Barang "+ubah+")");
                            }
                        break;
    
                        case 3:
                            System.out.print("Stok ["+result.getInt("stok")+"]\t: ");
                            Integer ganti3 = input.nextInt();
                            //update data  database database_gudang tabel barang
                            sql = "UPDATE barang SET stok = "+ganti3+" WHERE id_barang ='"+ubah+"'";
                            input.nextLine();
                            if(statement.executeUpdate(sql) > 0)
                            {
                                System.out.println("Berhasil memperbaharui stok (ID Barang "+ubah+")");
                            }
                        break;
    
                        default :
                            System.out.println("Inputan harus berupa angka 1/2/3 saja");
                        break;
                    }
                }
                else
                {
                    System.out.println("ID barang tidak ditemukan");
                }
            }
        }
        //exeption SQL 
        catch (SQLException e)
        {
            System.err.println("Kesalahan update data");
        }
        //exception input tidak sesuai jenis data
        catch (InputMismatchException e)
        {
            System.err.println("Inputan harus berupa angka!");
        }
    }

    
    public void delete() throws SQLException 
    {
    
    }

    public void save() throws SQLException 
    {
        
    }

    public void search() throws SQLException 
    {
        
    }

}
