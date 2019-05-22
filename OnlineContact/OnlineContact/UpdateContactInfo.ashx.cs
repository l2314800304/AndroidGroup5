using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Text;

namespace OnlineContact
{
    /// <summary>
    /// 修改联系人详情
    /// </summary>
    public class UpdateContactInfo : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            //获取请求参数
            int contact_info_ID = Convert.ToInt16(context.Request["contact_info_ID"]);
            string contact_number = context.Request["contact_number"].ToString(),
              contact_email = context.Request["contact_email"].ToString(),
              contact_type = context.Request["contact_type"].ToString();

            StringBuilder stb = new StringBuilder();
            stb.Append("update contact_info set Number= '");
            stb.Append(contact_number);
            stb.Append("',");
            stb.Append("EmailorNumber='");
            stb.Append(contact_email);
            stb.Append("',");
            stb.Append("Type='");
            stb.Append(contact_type);
            stb.Append("' where ID=");
            stb.Append(contact_info_ID);
             
            MySqlHelper helper = new MySqlHelper();
            if (helper.getMySqlCom(stb.ToString(), null) > 0)
            {
                //返回响应信息
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