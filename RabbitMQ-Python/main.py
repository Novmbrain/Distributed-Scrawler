import argparse
import json
import os
import sys
import pika
import scrawler

def main(sub_name):
    result = scrawler.get_datas_with_given_subreddit(sub_name)
    json_message = json.dumps(result)
    print(json_message)
    send_to_channel(json_message, sub_name)

def send_to_channel(message, sub_name):
    connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
    channel = connection.channel()
    channel.queue_declare(queue='results_queue')
    channel.basic_publish(exchange='', routing_key='results_queue', body=message)

    print(" [x] Sent results of %s" % sub_name)
    connection.close()

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Run a spider for a given URL')
    parser.add_argument('name', help='title of subreddit you want to search')
    args = parser.parse_args()
    try:
        main(args.name)
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)