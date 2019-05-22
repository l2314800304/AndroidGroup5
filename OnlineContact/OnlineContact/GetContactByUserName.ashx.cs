using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnlineContact
{
    /// <summary>
    /// GetContactByUserName 的摘要说明
    /// </summary>
    public class GetContactByUserName : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            String UserName = context.Request["UserName"];
            String sql = "select * from user where UserName=@u;";
            MySqlParameter[] pms ={
                new MySqlParameter("@u",UserName)
            };
            MySqlHelper helper = new MySqlHelper();
            MySqlDataReader reader = helper.getMySqlReader(sql, pms);
            if (reader.HasRows)
            {
                reader.Read();
                int id = reader.GetInt32(0);
                string u = reader.GetString(1), p = reader.GetString(2), s = reader.GetString(3), l = reader.GetString(4), r = reader.GetString(5);
                string res = "{\"ID\":" + id + ",\"UserName\":\"" + u + "\",\"Password\":\"" + p + "\",\"Sex\":\"" + s + "\",\"Location\":\"" + l + "\",\"Remark\":\"" + r + "\",\"Contact\":[";
                sql = "select * from contact where User_ID=@id;";
                reader.Close();
                helper.mysqlcom.Dispose();
                helper.mysqlcon.Close();
                helper.mysqlcon.Dispose();
                MySqlHelper helper1 = new MySqlHelper();
                MySqlDataReader reader1 = helper1.getMySqlReader(sql, new MySqlParameter("@id",id));
                if (reader1.HasRows)
                {
                    reader1.Read();
                    res+= "{\"ID\":" + reader1.GetInt32(0) + ",\"Name\":\"" + reader1.GetString(2) + "\",\"Birthday\":\"" + (reader1.IsDBNull(3)?"":reader1.GetString(3)) + "\",\"Contact_Info\":[";
                    MySqlHelper helper2 = new MySqlHelper();
                    MySqlDataReader reader2 = helper2.getMySqlReader("select * from contact_info where Contact_ID=@id", new MySqlParameter("@id", reader1.GetInt32(0) + ""));
                    if (reader2.HasRows)
                    {
                        reader2.Read();
                        res += "{\"ID\":" + reader2.GetInt32(1) + ",\"EmailOrNumber\":" + reader2.GetInt32(0) + ",\"Number\":\"" + reader2.GetString(2) + "\",\"Type\":\"" + reader2.GetString(3) + "\"}";
                    }
                    while (reader2.Read())
                    {
                        res += ",{\"ID\":" + reader2.GetInt32(1) + ",\"EmailOrNumber\":" + reader2.GetInt32(0) + ",\"Number\":\"" + reader2.GetString(2) + "\",\"Type\":\"" + reader2.GetString(3) + "\"}";
                    }
                    res += "]}";
                    reader2.Close();
                    helper2.mysqlcom.Dispose();
                    helper2.mysqlcon.Close();
                    helper2.mysqlcon.Dispose();
                }
                while (reader1.Read())
                {
                    res+= ",{\"ID\":" + reader1.GetInt32(0) + ",\"Name\":\"" + reader1.GetString(2) + "\",\"Birthday\":\"" + (reader1.IsDBNull(3) ? "" : reader1.GetString(3)) + "\",\"Contact_Info\":[";
                    MySqlHelper helper2 = new MySqlHelper();
                    MySqlDataReader reader2 = helper2.getMySqlReader("select * from contact_info where Contact_ID=@id", new MySqlParameter("@id", reader1.GetInt32(0) + ""));
                    if (reader2.HasRows)
                    {
                        reader2.Read();
                        res += "{\"ID\":" + reader2.GetInt32(1) + ",\"EmailOrNumber\":" + reader2.GetInt32(0) + ",\"Number\":\"" + reader2.GetString(2) + "\",\"Type\":\"" + reader2.GetString(3) + "\"}";
                    }
                    while (reader2.Read())
                    {
                        res += ",{\"ID\":" + reader2.GetInt32(1) + ",\"EmailOrNumber\":" + reader2.GetInt32(0) + ",\"Number\":\"" + reader2.GetString(2) + "\",\"Type\":\"" + reader2.GetString(3) + "\"}";
                    }
                    res += "]}";
                    reader2.Close();
                    helper2.mysqlcom.Dispose();
                    helper2.mysqlcon.Close();
                    helper2.mysqlcon.Dispose();
                }
                res += "],\"Record\":[";
                sql = "select * from record where User_ID=@id;";
                reader1.Close();
                helper1.mysqlcom.Dispose();
                helper1.mysqlcon.Close();
                helper1.mysqlcon.Dispose();
                MySqlHelper helper3 = new MySqlHelper();
                MySqlDataReader reader3 = helper3.getMySqlReader(sql, new MySqlParameter("@id", id));
                if (reader3.HasRows)
                {
                    reader3.Read();
                    res += "{\"ID\":" + reader3.GetInt32(0) + ",\"Number\":\"" + reader3.GetString(1) + "\",\"Duration\":\"" + reader3.GetString(2) + "\",\"Date\":\"" + reader3.GetString(3).ToString() + "\",\"Type\":\"" + reader3.GetString(5) + "\"}";
                }
                while (reader3.Read())
                {
                    res += ",{\"ID\":" + reader3.GetInt32(0) + ",\"Number\":\"" + reader3.GetString(1) + "\",\"Duration\":\"" + reader3.GetString(2) + "\",\"Date\":\"" + reader3.GetString(3).ToString() + "\",\"Type\":\"" + reader3.GetString(5) + "\"}";
                }
                res += "]}";
                context.Response.Write(res);
                reader3.Close();
                helper3.mysqlcom.Dispose();
                helper3.mysqlcon.Close();
                helper3.mysqlcon.Dispose();
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