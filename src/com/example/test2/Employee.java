package com.example.test2;

/**
 * Created by g00284823 on 17/11/2015.
 */
public class Employee
{
    String FNAME;
    String MINIT;
    String LNAME;
    String SSN;
    String BDATE;
    String ADDRESS;
    String SEX;
    String SALARY;
    String SUPERSSN;
    String DNO;
    public Employee()
    {
        FNAME = "NULL";
        MINIT = "NULL";
        LNAME = "NULL";
        SSN = "NULL";
        BDATE = "NULL";
        ADDRESS = "NULL";
        SEX = "NULL";
        SALARY = "NULL";
        SUPERSSN = "NULL";
        DNO = "NULL";
    }
    public void setFNAME(String a)
    {
        FNAME = a;
    }
    public void setMINIT(String a)
    {
        MINIT = a;
    }
    public void setLNAME(String a)
    {
        LNAME = a;
    }
    public void setSSN(String a)
    {
        SSN = a;
    }
    public void setBDATE(String a)
    {
        BDATE = a;
    }
    public void setADDRESS(String a)
    {
        ADDRESS = a;
    }
    public void setSEX(String a)
    {
        SEX = a;
    }
    public void setSALARY(String a)
    {
        SALARY = a;
    }
    public void setSUPERSSN(String a)
    {
        SUPERSSN = a;
    }
    public void setDNO(String a)
    {
        DNO = a;
    }
    public String getFNAME()
    {
        return FNAME;
    }
    public String getLNAME()
    {
        return LNAME;
    }
    public String getMINIT()
    {
        return MINIT;
    }
    public String getSEX()
    {
        return SEX;
    }
    public String getSALARY()
    {
        return SALARY;
    }
    public String getADDRESS()
    {
        return ADDRESS;
    }
    public String getBDATE()
    {
        return BDATE;
    }
    public String getSSN()
    {
        return SSN;
    }
    public String getSUPERSSN()
    {
        return SUPERSSN;
    }
    public String getDNO()
    {
        return DNO;
    }
}
