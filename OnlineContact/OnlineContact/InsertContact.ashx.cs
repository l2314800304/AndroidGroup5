using MySql.Data.MySqlClient;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnlineContact
{
    /// <summary>
    /// InsertContact 的摘要说明
    /// </summary>
    public class InsertContact : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            
            context.Response.ContentType = "text/plain";
            int User_ID = Convert.ToInt32(context.Request["User_ID"]);
            String contact = context.Request["Contact"];
            Contact cont= JsonConvert.DeserializeObject<Contact>(contact);
            MySqlHelper helper1 = new MySqlHelper();
            MySqlDataReader reader1 = helper1.getMySqlReader("select count(*) from contact where User_ID=" + User_ID + ";");
            reader1.Read();
            int id = User_ID*10000+reader1.GetInt32(0);
            String sql = "insert into contact (ID,User_ID,Name) Values ("+id+","+User_ID+",\""+cont.Name+"\");";
            reader1.Close();
            reader1.Dispose();
            helper1.mysqlcom.Dispose();
            helper1.mysqlcon.Close();
            helper1.mysqlcon.Dispose();
            MySqlHelper helper = new MySqlHelper();
            if (helper.getMySqlCom(sql) > 0)
            {
                sql = "insert into contact_info (EmailOrNumber,Number,Type,Contact_ID) values ";
                if (cont.ContactInfos != null)
                {
                    for (int j = 0; j < cont.ContactInfos.Count; j++)
                    {
                        if (sql.Length < 75)
                        {
                            sql += "(" + cont.ContactInfos[j].EmailOrNumber + ",\"" + cont.ContactInfos[j].Number + "\",\"" + cont.ContactInfos[j].Type + "\"," + id + ")";
                        }
                        else
                        {
                            sql += ",(" + cont.ContactInfos[j].EmailOrNumber + ",\"" + cont.ContactInfos[j].Number + "\",\"" + cont.ContactInfos[j].Type + "\"," + id + ")";
                        }
                        if(j==cont.ContactInfos.Count-1)
                            if (helper.getMySqlCom(sql) > 0)
                                context.Response.Write("OK");
                            else
                                context.Response.Write("Error");
                    }
                }
                else
                {
                    context.Response.Write("OK");
                }
            }
            else
            {
                context.Response.Write("Error");
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