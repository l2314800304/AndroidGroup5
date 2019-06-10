using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnlineContact
{
    /// <summary>
    /// SetBirthday 的摘要说明
    /// </summary>
    public class SetBirthday : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            String ID = context.Request["Contact_ID"];
            String Birthday = context.Request["Birthday"];
            String sql = "update Contact set Birthday='"+Birthday+"' where ID='" +ID + "'";
            MySqlHelper helper = new MySqlHelper();
            int count = helper.getMySqlCom(sql);
            if (count > 0)
            {
                context.Response.Write("OK");
            }
            else
            {
                context.Response.Write("ERROR");
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