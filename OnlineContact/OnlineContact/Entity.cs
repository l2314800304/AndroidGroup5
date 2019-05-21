using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnlineContact
{
    public class ContactInfos
    {
        public string EmailOrNumber { get; set; }
        public string ID { get; set; }
        public string Number { get; set; }
        public string Type { get; set; }
    }
    public class Contact
    {
        public List<ContactInfos> ContactInfos { get; set; }
        public string ID { get; set; }
        public string Name { get; set; }
    }
    public class Record
    {
        public string Duration { get; set; }
        public string ID { get; set; }
        public string Number { get; set; }
        public string Date { get; set; }
        public string Type { get; set; }
    }
    public class RootObject
    {
        public List<Record> Record { get; set; }
        public List<Contact> Contact { get; set; }
    }
}