import requests
from bs4 import BeautifulSoup
from pathlib import Path
import os
URL = "http://users.ics.forth.gr/~jsaveta/.index.php?dir=OAEI_HOBBIT_LinkDiscovery_2021/Spatial"
page = requests.get(URL)
soup = BeautifulSoup(page.content, "html.parser")

folders = soup.find_all("a")

for folder in folders:
    soup = BeautifulSoup(requests.get(URL+"/"+folder.text).content, "html.parser")  
    files = soup.find_all('a')
    for file in files:
        soup = BeautifulSoup(requests.get(URL+"/"+folder.text+"/"+file.text).content, "html.parser")
        fs = soup.find_all('a')
        for f in fs:       
            soup = BeautifulSoup(requests.get(URL+"/"+folder.text+"/"+file.text+"/"+f.text).content, "html.parser")
            rdfs = soup.find_all('a')
            for rdf in rdfs:
                rdf_url = URL+"/"+folder.text+"/"+file.text+"/"+f.text+"/"+rdf.text
                rdf_url.replace(".index.php?dir=","")
                rdf_file = requests.get(rdf_url)
                rdf_path = "/Users/vinci/WorkProjects/scholarship/oeg/OAEI_HOBBIT_LINKDISCOVERY_2021/"+folder.text+"/"+file.text+"/"+f.text+"/"+rdf.text
                with open(rdf_path, 'w+') as rdf_writer:
                    os.makedirs(Path("/Users/vinci/WorkProjects/scholarship/oeg/OAEI_HOBBIT_LINKDISCOVERY_2021/"+folder.text+"/"+file.text+"/"+f.text))
                    rdf_writer.write(rdf_file.content)




