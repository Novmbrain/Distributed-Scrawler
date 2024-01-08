import argparse
import json
import os
import sys
import threading
import pika
import scrawler

def fuzzy_search(keyword):
    subreddits = scrawler.get_subreddits_with_keyword(keyword)
    print(subreddits)

    threads = []
    for subreddit in subreddits:
        thread = threading.Thread(
            target=specific_search, args=(subreddit,))
        threads.append(thread)
        thread.start()

    for thread in threads:
        thread.join()

def specific_search(subreddit):
    result = scrawler.get_datas_with_given_subreddit(subreddit)
    json_message = json.dumps(result)
    # print(json_message)
    send_to_channel(json_message, subreddit)

def send_to_channel(message, sub_name):
    connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
    channel = connection.channel()
    channel.queue_declare(queue='results_queue', durable=True)
    channel.basic_publish(exchange='', 
                          routing_key='results_queue', 
                          body=message, 
                          properties=pika.BasicProperties(delivery_mode = pika.DeliveryMode.Persistent))

    print(" [x] Sent results of %s" % sub_name)
    connection.close()

if __name__ == '__main__':
    # parser = argparse.ArgumentParser(description='Run a spider for a given keyword')
    # parser.add_argument('keyword', help='what you want to search')
    # args = parser.parse_args()
    try:
        print("Please choose search type: ")
        print("1. Specific search")
        print("2. Fuzzy search")
        choice = input("Input your choice (1 or 2) : ")
        if choice == '1':
            subreddit = input("Please input subreddit name : ")
            specific_search(subreddit)
        elif choice == '2':
            keyword = input("Please input keyword : ")
            fuzzy_search(keyword)
        else:
            print("Invalid option")
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)