#!/usr/bin/env python3
import subprocess, pathlib, csv
from pytube import YouTube

def get_all_links():
    links_obj = []
    with open("musics_links.csv", 'r') as f:
        for obj in csv.reader(f):
            links_obj.append( obj )
    return links_obj

def dl_all_musics( links_obj ):
    downloaded_files = []
    counter = 0
    for obj in links_obj:
        link = obj[0]
        file_downloaded = YouTube( link ).streams \
        .filter(subtype='mp4') \
        .get_highest_resolution() \
        .download()
        downloaded_files.append(
            [pathlib.Path(file_downloaded).name.__str__(), links_obj[counter][1]]
        )
        print(f"\t[Download {counter+1}] - [{downloaded_files[-1][0]}]")
        counter += 1
    return downloaded_files

def extract_audio_files( downloaded_files ):
    counter = 1
    for file_info in downloaded_files:
        if pathlib.Path(file_info[1]).is_file():
            print("MP3 file [{output_file}] already exists. (No convertion done)")
        else:
            print(f"\t[{counter}] - <mp4 file> -> [{file_info[1]}]", end='')
            subprocess.run(["ffmpeg", "-i", file_info[0], file_info[1]],
                stdout=subprocess.DEVNULL,
                stderr=subprocess.STDOUT)
            print(" Done.")
        counter += 1

def delete_mp4_files():
    print("\nDeleting all mp4 files", end='')
    subprocess.run(["./clean_directory.sh"])
    print(" Done.")

if __name__ == '__main__':
    # Retrieve all music links provided :
    links = get_all_links()

    # Download all mp4 files :
    print("Staring to download musics' mp4 files ...")
    print("\tThis may take a while ... please wait.")
    downloaded_files = dl_all_musics( links )

    # Convert mp4 to mp3 files :
    print("\nStarting to extract audio from mp4 file.")
    print("  *Conversion in progress ...")
    extract_audio_files( downloaded_files )

    #Delete mp4 files :
    delete_mp4_files()