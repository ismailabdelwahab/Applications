#!/usr/bin/env python3

import sys, os, subprocess, requests

def check_arguments():
	if( len(sys.argv) != 2 ):
		print(f"Usage: {sys.argv[0]} <link from readcomiconline.li>")
		sys.exit(1)

def retrieve_html_page_data( page_link, created_html_file ):
	print(" [+] Downloading HTML page ...", end='')
	res = requests.get(page_link)
	with open(created_html_file,'w') as f:
		f.write(res.text)
	print(" Done.")
	
def create_comic_folder():	
	print(" [+] Creating the folder [comic/] if needed ...", end='')
	if( not os.path.isdir("comic") ):
		subprocess.run(["mkdir", "comic"])
	print(" Done.")

def retrieve_all_pages_links(created_html_file):
	####### Read html page :
	print("\n [+] Reading HTML page ...", end='')
	with open( created_html_file, 'r') as f:
		lines = f.read().split('\n')
	print(" Done.")
	####### Retrieve interresting images' links + parse them :
	print(" [+] Parsing images' links ...", end='')
	image_links = []
	for line in lines :
		if( line.replace(' ','').startswith("lstImages.push(") ):
			image_links.append( line[8+len("lstImages.push(\""):-3])
	####### Remove duplicates
	image_links = list( dict.fromkeys(image_links) )
	print(" Done.")
	return image_links

def download_all_pages( image_links ):
	print(" [+] Starting to download pages:")
	page_number = 1
	for image_link in image_links:
		print(f"\t[+] Dowloading page {page_number} ... ", end = '')
		dl_page( image_link, page_number )
		print("Done.")
		page_number += 1
	print("All pages were downloaded and are availiable in [comic/].")

def dl_page(link, page_number):
	command = ["wget", "-O", f"comic/page{page_number}.jpg", f"{link}"]
	process = subprocess.run(command, 
		stdout=subprocess.DEVNULL,
		stderr=subprocess.DEVNULL)

if __name__ == '__main__':
	##### Check if argument line provides the necessary link :
	check_arguments()
	##### Declare variables :
	page_link = sys.argv[1]
	created_html_file = "comic_html_page.html"
	##### Get comic's HTML page content :
	retrieve_html_page_data( page_link, created_html_file )
	##### Create comic folder :
	create_comic_folder()
	##### Retrieve all img's pages :
	image_links = retrieve_all_pages_links( created_html_file )
	##### Download all pages :
	download_all_pages( image_links )