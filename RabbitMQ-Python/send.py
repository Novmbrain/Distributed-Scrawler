import pika


def send_message_to_queue(queue_name, message, host='localhost'):
    """Send a message to a specified queue."""

    connection = pika.BlockingConnection(pika.ConnectionParameters(host=host))
    channel = connection.channel()

    channel.queue_declare(queue=queue_name)

    channel.basic_publish(exchange='', routing_key=queue_name, body=message)
    print(f" [x] Sent '{message}' to queue '{queue_name}'")

    connection.close()


