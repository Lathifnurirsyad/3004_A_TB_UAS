//class Vendor
public class Vendor extends Orang
{
    String noHP, alamat, id_vendor;
    
    //constructor
    public Vendor()
    {

    }

    //constuctor
    public Vendor(String nama, String id_vendor) 
    {
        this.nama = nama;
        this.id_vendor = id_vendor;
        System.out.println(this.nama+" adalah Vendor baru kita");
    }   

    public void methodKosong()
    {

    }
}