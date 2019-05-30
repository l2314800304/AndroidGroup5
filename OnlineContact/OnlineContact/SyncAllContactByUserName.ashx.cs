using MySql.Data.MySqlClient;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnlineContact
{
    /// <summary>
    /// SyncAllContactByUserName 的摘要说明
    /// </summary>
    public class SyncAllContactByUserName : IHttpHandler
    {
        RootObject Check(RootObject rb_local)
        {
            if(rb_local.Contact!=null)
            for (int i = 0; i < rb_local.Contact.Count; i++)
            {
                for (int j = i + 1; j < rb_local.Contact.Count; j++)
                {
                    if (rb_local.Contact[i].Name.Equals(rb_local.Contact[j].Name) )
                    {
                        if (rb_local.Contact[i].ContactInfos != null && rb_local.Contact[i].ContactInfos.Count == rb_local.Contact[j].ContactInfos.Count)
                            for (int m = 0; m < rb_local.Contact[i].ContactInfos.Count; m++)
                            {
                                if (!rb_local.Contact[i].ContactInfos[m].Number.Equals(rb_local.Contact[j].ContactInfos[m].Number))
                                {
                                    m = rb_local.Contact[i].ContactInfos.Count;
                                }
                                else if (m == rb_local.Contact[i].ContactInfos.Count - 1)
                                {
                                    rb_local.Contact.RemoveAt(j);
                                    m = rb_local.Contact[i].ContactInfos.Count;
                                    j--;
                                }
                            }
                    }
                    else
                    {
                        if(rb_local.Contact[i].ContactInfos!=null)
                        for (int m = 0; m < rb_local.Contact[i].ContactInfos.Count; m++)
                        {
                            ContactInfos i1 = rb_local.Contact[i].ContactInfos[m];
                            for (int n = m + 1; n < rb_local.Contact[i].ContactInfos.Count; n++)
                            {
                                ContactInfos i2 = rb_local.Contact[i].ContactInfos[n];
                                if (i1.Number.Equals(i2.Number) && i1.Type.Equals(i2.Type) && i1.EmailOrNumber.Equals(i2.EmailOrNumber))
                                {
                                    rb_local.Contact[i].ContactInfos.RemoveAt(n);
                                    n--;
                                }
                            }
                        }
                    }
                }
            }
            return rb_local;
        }
        RootObject Check(RootObject rb_local, RootObject rb_cloud)
        {
            for (int i = 0; i < rb_local.Contact.Count; i++)
            {
                for (int j = i + 1; j < rb_local.Contact.Count; j++)
                {
                    if (rb_local.Contact[i].Name.Equals(rb_local.Contact[j].Name) && rb_local.Contact[i].ContactInfos.Count == rb_local.Contact[j].ContactInfos.Count)
                    {
                        for (int m = 0; m < rb_local.Contact[i].ContactInfos.Count; m++)
                        {
                            if (!rb_local.Contact[i].ContactInfos[m].Number.Equals(rb_local.Contact[j].ContactInfos[m].Number))
                            {
                                m = rb_local.Contact[i].ContactInfos.Count;
                            }
                            else if (m == rb_local.Contact[i].ContactInfos.Count - 1)
                            {
                                rb_local.Contact.RemoveAt(j);
                                m = rb_local.Contact[i].ContactInfos.Count;
                                j--;
                            }
                        }
                    }
                    else
                    {
                        for (int m = 0; m < rb_local.Contact[i].ContactInfos.Count; m++)
                        {
                            ContactInfos i1 = rb_local.Contact[i].ContactInfos[m];
                            for (int n = m + 1; n < rb_local.Contact[i].ContactInfos.Count; n++)
                            {
                                ContactInfos i2 = rb_local.Contact[i].ContactInfos[n];
                                if (i1.Number.Equals(i2.Number) && i1.Type.Equals(i2.Type) && i1.EmailOrNumber.Equals(i2.EmailOrNumber))
                                {
                                    rb_local.Contact[i].ContactInfos.RemoveAt(n);
                                    n--;
                                }
                            }
                        }
                    }
                }
            }
            return rb_local;
        }

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            String UserName = context.Request["UserName"];
            String contact = context.Request["Contact"];
            String record = context.Request["Record"];
            String res = "";
            RootObject rb_local = JsonConvert.DeserializeObject<RootObject>("{\"Contact\":" + contact + ",\"Record\":" + record + "}");
            rb_local = Check(rb_local);
            String sql = "select * from user where UserName=@u ORDER BY ID;";
            MySqlParameter[] pms ={
                new MySqlParameter("@u",UserName)
            };
            MySqlHelper helper = new MySqlHelper();
            MySqlDataReader reader = helper.getMySqlReader(sql, pms);
            RootObject rb_cloud = new RootObject();
            rb_cloud.Contact = new List<Contact>();
            rb_cloud.Record = new List<Record>();
            if (reader.HasRows)
            {
                reader.Read();
                int id = reader.GetInt32(0);
                String u = reader.GetString(1), p = reader.GetString(2), s = reader.GetString(3), l = reader.GetString(4), r = reader.GetString(5);
                reader.Dispose();
                helper.mysqlcom.Dispose();
                helper.mysqlcon.Close();
                helper.mysqlcon.Dispose();
                res = "{\"ID\":" + id + ",\"UserName\":\"" + u + "\",\"Password\":\"" + p + "\",\"Sex\":\"" + s + "\",\"Location\":\"" + l + "\",\"Remark\":\"" + r + "\",\"Contact\":[";
                sql = "select * from contact where User_ID=@id ORDER BY ID;";
                MySqlHelper helper1 = new MySqlHelper();
                MySqlDataReader reader1 = helper1.getMySqlReader(sql, new MySqlParameter("@id", id));
                while (reader1.Read())
                {
                    Contact con = new Contact();
                    con.ContactInfos = new List<ContactInfos>();
                    con.ID = reader1.GetInt32(0) + "";
                    con.Name = reader1.GetString(2);
                    con.Birthday = Convert.IsDBNull(reader1[3])?"":reader1.GetString(3);
                    MySqlHelper helper12 = new MySqlHelper();
                    MySqlDataReader reader12 = helper12.getMySqlReader("select * from contact_info where Contact_ID=" + reader1.GetInt32(0)+ "  ORDER BY ID");
                    List<ContactInfos> contactInfos = new List<ContactInfos>();
                    while (reader12.Read())
                    {
                        ContactInfos ci = new ContactInfos();
                        ci.EmailOrNumber = reader12.GetInt32(0) + "";
                        ci.ID = reader12.GetInt32(1) + "";
                        ci.Number = reader12.GetString(2);
                        ci.Type = reader12.GetString(3);
                        contactInfos.Add(ci);
                    }
                    con.ContactInfos = contactInfos;
                    rb_cloud.Contact.Add(con);
                    reader12.Dispose();
                    helper12.mysqlcom.Dispose();
                    helper12.mysqlcon.Close();
                    helper12.mysqlcon.Dispose();
                }
                reader1.Dispose();
                helper1.mysqlcom.Dispose();
                helper1.mysqlcon.Close();
                helper1.mysqlcon.Dispose();
                rb_cloud = Check(rb_cloud);
                sql = "select * from record where User_ID=@id ORDER BY ID;";
                MySqlHelper helper2 = new MySqlHelper();
                MySqlDataReader reader2 = helper2.getMySqlReader(sql, new MySqlParameter("@id", id));
                while (reader2.Read())
                {
                    Record rec = new Record();
                    rec.ID = reader2.GetInt32(0)+"";
                    rec.Number = reader2.GetString(1);
                    rec.Duration = reader2.GetString(2);
                    rec.Date = reader2.GetString(3);
                    rec.Type = reader2.GetString(5);
                    rb_cloud.Record.Add(rec);
                }
                reader2.Dispose();
                helper2.mysqlcom.Dispose();
                helper2.mysqlcon.Close();
                helper2.mysqlcon.Dispose();
                Contact a, b;
                ContactInfos c,d;
                if(rb_cloud.Contact.Count>0)
                    for (int i = 0; i < rb_cloud.Contact.Count; i++)
                    {
                        bool flag = false;
                        for (int j=0; j < rb_local.Contact.Count; j++)
                        {
                            a = rb_local.Contact[j];
                            b = rb_cloud.Contact[i];
                            if (a.ContactInfos == null) a.ContactInfos = new List<ContactInfos>();
                            if (a.Name.Equals(b.Name)&&a.ContactInfos.Count==b.ContactInfos.Count)
                            {
                                if (a.ContactInfos.Count == 0)
                                {
                                    rb_local.Contact.RemoveAt(j);
                                    flag = true;
                                    j--;
                                }else
                                    for(int k = 0; k < a.ContactInfos.Count; k++)
                                    {
                                        c = a.ContactInfos[k];
                                        d = b.ContactInfos[k];
                                        if (!c.EmailOrNumber.Equals(d.EmailOrNumber)||!c.Number.Equals(d.Number
                                            ) ||! c.Type.Equals(d.Type))
                                        {
                                            k = a.ContactInfos.Count;
                                        }
                                        else if(k == a.ContactInfos.Count - 1)
                                        {
                                            rb_local.Contact.RemoveAt(j);
                                            flag = true;
                                            j--;
                                        }
                                    }
                            }
                        }
                        if (flag == true)
                        {
                            if(rb_cloud.Contact.Count>i)
                                for(int k = i+1; k < rb_cloud.Contact.Count; k++)
                                {
                                    a = rb_cloud.Contact[k];
                                    b = rb_cloud.Contact[i];
                                    if (a.ContactInfos == null) a.ContactInfos = new List<ContactInfos>();
                                    if (a.Name.Equals(b.Name) && a.ContactInfos.Count == b.ContactInfos.Count)
                                    {
                                        if (a.ContactInfos.Count == 0)
                                        {
                                            rb_cloud.Contact.RemoveAt(k);
                                            flag = true;
                                            k--;
                                        }
                                        for (int m = 0; m < a.ContactInfos.Count; m++)
                                        {
                                            for (int n = 0; m < b.ContactInfos.Count; n++)
                                            {
                                                c = a.ContactInfos[m];
                                                d = b.ContactInfos[n];
                                                if (c.EmailOrNumber.Equals(d.EmailOrNumber) && c.Number.Equals(d.Number
                                                    ) && c.Type.Equals(d.Type) && m == a.ContactInfos.Count - 1)
                                                {
                                                    rb_cloud.Contact.RemoveAt(k);
                                                    flag = true;
                                                    k--;
                                                }
                                                else if (c.EmailOrNumber.Equals(d.EmailOrNumber) && c.Number.Equals(d.Number) && c.Type.Equals(d.Type))
                                                { }
                                                else
                                                {
                                                    k = a.ContactInfos.Count;
                                                }
                                            }
                                        }
                                    }
                                }
                            rb_cloud.Contact.RemoveAt(i);
                            i--;
                        }
                    }
                Record r1, r2;
                if (rb_cloud.Record.Count > 0)
                    for (int i = 0; i < rb_cloud.Record.Count; i++)
                    {
                        bool flag = false;
                        for (int j = 0; j < rb_local.Record.Count; j++)
                        {
                            r1 = rb_local.Record[j];
                            r2 = rb_cloud.Record[i];
                            if (r1.Type.Equals(r2.Type)&&r1.Number.Equals(r2.Number)&&r1.ID.Equals(r2.ID)&&r1.Duration.Equals(r2.Duration)&&r1.Date.Equals(r2.Date))
                            {
                                rb_local.Record.RemoveAt(j);
                                j--;
                                flag = true;
                            }
                        }
                        if (flag)
                        {
                            rb_cloud.Record.RemoveAt(i);
                            i--;
                        }
                    }

                MySqlHelper helper3 = new MySqlHelper();
                string ss = "insert into contact (ID,User_ID,Name) values ";
                sql = "insert into contact_info (EmailOrNumber,Number,Type,Contact_ID) values ";
                if (rb_local.Contact != null)
                {
                    for (int i = 0; i < rb_local.Contact.Count; i++)
                    {
                        if (i == 0)
                        {
                            ss += "(" + (int)(id * 10000 + i) + "," + id + ",\"" + rb_local.Contact[i].Name + "\")";
                        }
                        else
                        {
                            ss += ",(" + (int)(id * 10000 + i) + "," + id + ",\"" + rb_local.Contact[i].Name + "\")";
                        }
                        if (rb_local.Contact[i].ContactInfos != null)
                            for (int j = 0; j < rb_local.Contact[i].ContactInfos.Count; j++)
                            {
                                if (sql.Length < 75)
                                {
                                    sql += "(" + rb_local.Contact[i].ContactInfos[j].EmailOrNumber + ",\"" + rb_local.Contact[i].ContactInfos[j].Number + "\",\"" + rb_local.Contact[i].ContactInfos[j].Type + "\"," + (int)(id * 10000 + i) + ")";
                                }
                                else
                                {
                                    sql += ",(" + rb_local.Contact[i].ContactInfos[j].EmailOrNumber + ",\"" + rb_local.Contact[i].ContactInfos[j].Number + "\",\"" + rb_local.Contact[i].ContactInfos[j].Type + "\"," + (int)(id * 10000 + i) + ")";
                                }

                            }
                    }
                    if (rb_local.Contact.Count != 0)
                    {
                        helper3.getMySqlCom(ss);
                    }
                    if (rb_local.Contact.Count != 0)
                    {
                        helper3.getMySqlCom(sql);
                    }
                    
                }
                ss = "insert into record (Number,Duration,Date,User_ID,Type) values ";
                if (rb_local.Record != null)
                {
                    for (int i = 0; i < rb_local.Record.Count; i++)
                    {
                        if (i == 0)
                        {
                            ss += "(\"" + rb_local.Record[i].Number + "\",\"" + rb_local.Record[i].Duration + "\",\"" + rb_local.Record[i].Date + "\"," + id + ",\"" + rb_local.Record[i].Type + "\")";
                        }
                        else
                        {
                            ss += ",(\"" + rb_local.Record[i].Number + "\",\"" + rb_local.Record[i].Duration + "\",\"" + rb_local.Record[i].Date + "\"," + id + ",\"" + rb_local.Record[i].Type + "\")";
                        }

                    }
                    if (rb_local.Record.Count != 0)
                    {
                        helper3.getMySqlCom(ss);
                    }
                }
                rb_local.Record.AddRange(rb_cloud.Record);
                rb_local.Contact.AddRange(rb_cloud.Contact);
                if (rb_local.Contact != null)
                {
                    for (int i = 0; i < rb_local.Contact.Count; i++)
                    {
                        if (i == 0)
                        {
                            res += "{\"ID\":" + (int)(id * 10000 + i) + ",\"Name\":\"" + rb_local.Contact[i].Name + "\",\"Birthday\":\""+rb_local.Contact[i].Birthday+"\",\"ContactInfos\":[";
                        }
                        else
                        {
                            res += ",{\"ID\":" + (int)(id * 10000 + i) + ",\"Name\":\"" + rb_local.Contact[i].Name + "\",\"Birthday\":\"" + rb_local.Contact[i].Birthday + "\",\"ContactInfos\":[";
                        }
                        if (rb_local.Contact[i].ContactInfos != null)
                            for (int j = 0; j < rb_local.Contact[i].ContactInfos.Count; j++)
                            {
                                if (j==0)
                                {
                                    res += "{\"ID\":" + 0 + ",\"EmailOrNumber\":" + rb_local.Contact[i].ContactInfos[j].EmailOrNumber + ",\"Number\":\"" + rb_local.Contact[i].ContactInfos[j].Number + "\",\"Type\":\"" + rb_local.Contact[i].ContactInfos[j].Type + "\"}";
                                }
                                else
                                {
                                    res += ",{\"ID\":" + 0 + ",\"EmailOrNumber\":" + rb_local.Contact[i].ContactInfos[j].EmailOrNumber + ",\"Number\":\"" + rb_local.Contact[i].ContactInfos[j].Number + "\",\"Type\":\"" + rb_local.Contact[i].ContactInfos[j].Type + "\"}";
                                }

                            }
                        res += "]}";
                    }

                }
                res += "],\"Record\":[";
                if (rb_local.Record != null)
                {
                    for (int i = 0; i < rb_local.Record.Count; i++)
                    {
                        if (i == 0)
                        {
                            res += "{\"ID\":0,\"Number\":\"" + rb_local.Record[i].Number + "\",\"Duration\":\"" + rb_local.Record[i].Duration + "\",\"Date\":\"" + rb_local.Record[i].Date + "\",\"Type\":\"" + rb_local.Record[i].Type + "\"}";
                        }
                        else
                        {
                            res += ",{\"ID\":0,\"Number\":\"" + rb_local.Record[i].Number + "\",\"Duration\":\"" + rb_local.Record[i].Duration + "\",\"Date\":\"" + rb_local.Record[i].Date + "\",\"Type\":\"" + rb_local.Record[i].Type + "\"}";
                        }

                    }
                }
                res += "]}";
                context.Response.Write(res);
                reader.Close();
                helper.mysqlcom.Dispose();
                helper.mysqlcon.Close();
                helper.mysqlcon.Dispose();
                reader1.Close();
                helper1.mysqlcom.Dispose();
                helper1.mysqlcon.Close();
                helper1.mysqlcon.Dispose();
            }
            else
            {
                context.Response.Write("Error");
            }
            if (!reader.IsClosed)
            {
                reader.Close();
                helper.mysqlcom.Dispose();
                helper.mysqlcon.Close();
                helper.mysqlcon.Dispose();
            }
        }

        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
    }
}