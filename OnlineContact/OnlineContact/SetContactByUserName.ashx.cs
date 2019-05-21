using MySql.Data.MySqlClient;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnlineContact
{
    /// <summary>
    /// SetContactByUserName 的摘要说明
    /// </summary>
    public class SetContactByUserName : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            String UserName = context.Request["UserName"];
            String contact = context.Request["Contact"];
            String record = context.Request["Record"];
            RootObject rb = JsonConvert.DeserializeObject<RootObject>("{\"Contact\":"+contact+",\"Record\":"+record+"}");
            String sql = "select ID from user where UserName=@u;";
            MySqlParameter[] pms ={
                new MySqlParameter("@u",UserName)
            };
            MySqlHelper helper = new MySqlHelper();
            MySqlDataReader reader = helper.getMySqlReader(sql, pms);
            if (reader.HasRows)
            {
                reader.Read();
                int id = reader.GetInt32(0);
                sql = "select * from contact where User_ID=@id;";
                reader = helper.getMySqlReader(sql, new MySqlParameter("@id", id));
                MySqlHelper helper1 = new MySqlHelper();
                if (reader.HasRows)
                {
                    while (reader.Read())
                    {
                        int a1 = helper1.getMySqlCom("delete from contact_info where Contact_ID=@id", new MySqlParameter("@id", reader.GetInt32(0) + ""));
                    }
                }
                int a = helper1.getMySqlCom("delete from contact where User_ID=@id", new MySqlParameter("@id", id + ""));
                string ss = "insert into record (Number,Duration,Date,User_ID,Type) values ";
                for (int i = 0; i < rb.Record.Count; i++)
                {
                    if (i == 0)
                        ss += "(\""+ rb.Record[i].Number + "\",\"" + rb.Record[i].Duration + "\",\"" + rb.Record[i].Date + "\"," + id + ",\"" + rb.Record[i].Type + "\")";
                    else
                        ss += ",(\"" + rb.Record[i].Number + "\",\"" + rb.Record[i].Duration + "\",\"" + rb.Record[i].Date + "\"," + id + ",\"" + rb.Record[i].Type + "\")";
                    
                }
                if (rb.Record.Count!=0&&helper1.getMySqlCom(ss) <= 0)
                {
                    context.Response.Write("Error");
                    return;
                }
                ss = "insert into contact (ID,User_ID,Name) values ";
                sql = "insert into contact_info (EmailOrNumber,Number,Type,Contact_ID) values ";
                for (int i = 0; i < rb.Contact.Count; i++)
                {
                    if (i == 0)
                        ss += "(" + (int)(id * 10000 + i) + "," + id + ",\"" + rb.Contact[i].Name+"\")";
                    else
                        ss += ",(" + (int)(id * 10000 + i) + "," + id + ",\"" + rb.Contact[i].Name + "\")";
                    
                    for (int j = 0; j < rb.Contact[i].ContactInfos.Count; j++)
                    {
                        if (sql.Length==71)
                            sql += "(" + rb.Contact[i].ContactInfos[j].EmailOrNumber + ",\"" + rb.Contact[i].ContactInfos[j].Number + "\",\"" + rb.Contact[i].ContactInfos[j].Type + "\","+ (int)(id * 10000 + i) + ")";
                        else
                            sql += ",(" + rb.Contact[i].ContactInfos[j].EmailOrNumber + ",\"" + rb.Contact[i].ContactInfos[j].Number + "\",\"" + rb.Contact[i].ContactInfos[j].Type + "\"," + (int)(id * 10000 + i) + ")";
                        
                    }
                }
                if (rb.Contact.Count!=0&&helper1.getMySqlCom(ss) <= 0)
                {
                    context.Response.Write("Error");
                    return;
                }else if (rb.Contact.Count != 0)
                {
                    if (sql.Length>71&&helper1.getMySqlCom(sql) <= 0)
                    {
                        context.Response.Write("Error");
                        return;
                    }
                }
                context.Response.Write("OK");
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