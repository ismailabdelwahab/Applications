#!/usr/bin/env python3
import subprocess
from time import sleep
from threading import Thread
import os

SERVERS_DIR = "Servers/"
SERVER_TESTED = "ChatamuCentral"
CLIENTS_DIR = "Clients/"
DEFAULT_PORT = 12345

########### Printing ###############################
def printBanner( banner_title, banner_content ):
    sleep(1)
    print("\n"+"#"*20+" "+banner_title+" "+"#"*23+"\n"+\
    banner_content +"\n"+"#"*60)
    sleep(1) 

def print_server_log():
    printBanner( "Expected output", "The following will be the output of one of the three servers:\n\
    Reference table: server port || clients connected to this server:\n\
        12345 || client0 AND client1\n\
        12346 || client2\n\
        12347 || client3\n\
    Based on the first line of the output you will know which server's output it is. And even if you are on the server with port 12347 you can see the output of client0 and client 1 that are connected to server with port 12345 for example.")
    server_log = run_command( ['cat','server.log'] )
    print( server_log.stdout.decode() )

############# Manage data ##########################
def get_text_from_file( filename ):
    with open( filename , 'r') as f:
    	lines = f.read()
    return lines

########### shell commands #########################
def run_command( command , file_to_stdin=""):
    """ Returns the process that is running the command """
    return subprocess.run( command, stdout=subprocess.PIPE )

############ SERVERS ###############################
def launchThreeServers():
    servers_ports = [x for x in range(12345,12348)]
    servers_pids = []
    for port in servers_ports:
        servers_pids.append( launchChatamuFederatedServer(port) )
    return servers_pids
#def launchChatamuFederatedServer( server_port ):
#    server_cmd=["./server.sh", "ChatamuFederated", server_port] 
def launchChatamuFederatedServer( server_port ):
    """ Return the PID of the process running the server. """
    server_path= SERVERS_DIR + SERVER_TESTED +"/" + SERVER_TESTED
    server_cmd=["./pm_daemonize.sh", "java", server_path, str(server_port)]
    daemonOutput = run_command( server_cmd )
    server_pid = daemonOutput.stdout.decode()[:-1]
    return server_pid

def killChatamuServer( server_pid ):
    run_command(["kill", str(server_pid)])
def killAllThreeServers( servers_pids ):
    for pid in servers_pids:
        killChatamuServer( pid )
################## Clients #########################
def launchSimpleClient():
    """ Connect to localhost 12345 """
    # Get client text for test files:
    lines = get_text_from_file( '../../Tests/txt_sources/client0.txt' )
    #Connect the client:
    client_cmd = ["./client.sh", "SimpleClient"]
    client = subprocess.Popen( client_cmd,stdout= subprocess.PIPE,
	        stderr = subprocess.PIPE, stdin = subprocess.PIPE)
    #Send the text of the client:
    client_output = client.communicate( lines.encode() )
    client.kill()
def launchNormalClient( client_text_file ):
    # Get client text for test files:
    lines = get_text_from_file( '../../Tests/txt_sources/'+client_text_file )
    #Connect the client:
    client_cmd = ["./client.sh", "NormalClient"]
    client = subprocess.Popen( client_cmd,stdout= subprocess.PIPE,
	        stderr = subprocess.PIPE, stdin = subprocess.PIPE)
    #Send the text of the client:
    client_output = client.communicate( lines.encode() )
    client.kill()

def launchAllClients():
    client_T = []
    client_T.append( Thread(target=launchSimpleClient) )
    client_T.append( Thread(target=launchNormalClient, 
                args=('client1.txt',)) )
    client_T.append(Thread(target=launchNormalClient, 
                args=('client2.txt',)) )
    client_T.append(Thread(target=launchNormalClient, 
                args=('client3.txt',)) )
    for cl_thread in client_T:
        cl_thread.start()
    for cl_thread in client_T:
        cl_thread.join()

############### Script #############################
def main():
    #Launch servers
    os.chdir( "../src/" )
    servers_pids = launchThreeServers()
    #Launch clients
    os.chdir( "Clients/" )
    sleep(1)
    launchAllClients()
    #Kill all servers and print one server's output
    sleep(2)
    killAllThreeServers( servers_pids )
    os.chdir( ".." )
    print_server_log()
    
if __name__ == '__main__':
    main()
