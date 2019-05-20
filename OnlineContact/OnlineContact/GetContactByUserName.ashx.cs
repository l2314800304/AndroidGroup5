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
                reader = helper.getMySqlReader(sql, new MySqlParameter("@id",id));
                string res = "{\"Contact\":[";
                MySqlHelper helper1 = new MySqlHelper();
                MySqlDataReader reader1 = null;
                if (reader.HasRows)
                {
                    reader.Read();
                    res+= "{\"ID\":\"" + reader.GetInt32(0) + "\",\"Name\":\"" + reader.GetString(2) + "\",\"Birthday\":\"" + reader.GetString(3) + "\",\"Contact_Info\":[";
                    reader1 = helper1.getMySqlReader("select * from contact_info where Contact_ID=@id", new MySqlParameter("@id", reader.GetInt32(0) + ""));
                    if (reader1.HasRows)
                    {
                        reader1.Read();
                        res += "{\"ID\":\"" + reader1.GetInt32(1) + "\",\"EmailOrNumber\":\"" + reader1.GetInt32(0) + "\",\"Number\":\"" + reader1.GetString(2) + "\",\"Type\":\"" + reader1.GetString(3) + "\"}";
                    }
                    while (reader1.Read())
                    {
                        res += ",{\"ID\":\"" + reader1.GetInt32(1) + "\",\"EmailOrNumber\":\"" + reader1.GetInt32(0) + "\",\"Number\":\"" + reader1.GetString(2) + "\",\"Type\":\"" + reader1.GetString(3) + "\"}";
                    }
                    res += "]}";
                }
                while (reader.Read())
                {
                    res+= ",{\"ID\":\"" + reader.GetInt32(0) + "\",\"Name\":\"" + reader.GetString(2) + "\",\"Birthday\":\"" + reader.GetString(3) + "\",\"Contact_Info\":[";
                    reader1 = helper1.getMySqlReader("select * from contact_info where Contact_ID=@id", new MySqlParameter("@id", reader.GetInt32(0) + ""));
                    if (reader1.HasRows)
                    {
                        reader1.Read();
                        res += "{\"ID\":\"" + reader1.GetInt32(1) + "\",\"EmailOrNumber\":\"" + reader1.GetInt32(0) + "\",\"Number\":\"" + reader1.GetString(2) + "\",\"Type\":\"" + reader1.GetString(3) + "\"}";
                    }
                    while (reader1.Read())
                    {
                        res += ",{\"ID\":\"" + reader1.GetInt32(1) + "\",\"EmailOrNumber\":\"" + reader1.GetInt32(0) + "\",\"Number\":\"" + reader1.GetString(2) + "\",\"Type\":\"" + reader1.GetString(3) + "\"}";
                    }
                    res += "]}";
                }
                res += "],\"Record\":[";
                sql = "select * from record where User_ID=@id;";
                reader = helper.getMySqlReader(sql, new MySqlParameter("@id", id));
                if (reader.HasRows)
                {
                    reader.Read();
                    res += "{\"ID\":\"" + reader.GetInt32(0) + "\",\"Number\":\"" + reader.GetString(1) + "\",\"Duration\":\"" + reader.GetString(2) + "\",\"Date\":\"" + reader.GetDateTime(3).ToString() + "\",\"Type\":\"" + reader.GetString(5) + "\"}";
                }
                while (reader.Read())
                {
                    res += ",{\"ID\":\"" + reader.GetInt32(0) + "\",\"Number\":\"" + reader.GetString(1) + "\",\"Duration\":\"" + reader.GetString(2) + "\",\"Date\":\"" + reader.GetDateTime(3).ToString() + "\",\"Type\":\"" + reader.GetString(5) + "\"}";
                }
                res += "]}";
                context.Response.Write(res);
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