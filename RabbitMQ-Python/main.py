import argparse
import json
import os
import sys
import threading
import pika
import scrawler

def main(keyword):
    subreddits = scrawler.get_subreddits_with_keyword(keyword)
    print(subreddits)

    threads = []
    for subreddit in subreddits:
        thread = threading.Thread(
            target=process_subreddit, args=(subreddit,))
        threads.append(thread)
        thread.start()

    for thread in threads:
        thread.join()

def process_subreddit(subreddit):
    result = scrawler.get_datas_with_given_subreddit(subreddit)
    json_message = json.dumps(result)
    print(json_message)
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
    parser = argparse.ArgumentParser(description='Run a spider for a given keyword')
    parser.add_argument('keyword', help='what you want to search')
    args = parser.parse_args()
    try:
        main(args.keyword)
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)