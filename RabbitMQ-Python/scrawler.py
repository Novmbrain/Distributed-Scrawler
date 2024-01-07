#import praw
import requests
import pandas as pd
from datetime import datetime

# Reddit API credentials
client_id = 'ofmJ9OCy0zZcIT2mSBVedw'
client_secret = '5jcbxlK3673PtwDpETFAULLbPj_Rug'
user_agent = 'MyApi/0.0.1'

auth=requests.auth.HTTPBasicAuth(client_id,client_secret)#授权

with open('pw.txt','r') as f:
    pw = f.read()
    
#data to login
data={
    'grant_type':'password',
    'username':'Uze_loge',
    'password': pw,
    'User-Agent':'MyApi/0.0.1'
}

headers={'User-Agent':'MyApi/0.0.1'}

res=requests.post('https://www.reddit.com/api/v1/access_token',auth=auth,data=data,headers=headers)#对令牌的请求，给地址 https://www.reddit.com/api/v1/access_token发送请求
reponse = res.json()
# print(reponse)
token=reponse['access_token']
#print('the token is',token)
headers['authorization']=f'bearer {token}' #add access_token to header

url = 'https://oauth.reddit.com/r/'

def get_datas_with_given_subreddit(name):
    res = {}
    get_information(name, res)
    get_posts(name, res)
    # print(res)
    return res

def get_information(name, result):
    global url
    sub_url = url + name + '/about'
    print(sub_url)
    res = requests.get(sub_url, headers=headers)

    if res.status_code != 200:
        print(f"Error: {res.status_code}")
    else:
        data = res.json()['data']
        result['sub_name']=data['display_name']
        result['subscribers']=data['subscribers']
        result['accounts_active']=data['accounts_active']
    # print(result)

def get_posts(name, result):
    global url
    sub_url = url + name
    print(sub_url)
    res = requests.get(sub_url, headers=headers)

    if res.status_code != 200:
        print(f"Error: {res.status_code}")
    else:
        res_posts = []
        result['posts'] = res_posts
        posts = res.json()['data']['children']
        for post in posts:
            score = post['data']['score']
            upvote_ratio = post['data']['upvote_ratio']
            upvotes = int(round(score / upvote_ratio))
            downvotes = upvotes - score

            res_posts.append({
                'title': post['data']['title'],
                'upvotes': upvotes,
                'downvotes': downvotes,
                'num_comments': post['data']['num_comments'],
                'created_utc': post['data']['created_utc']
            })

# get_datas_with_given_subreddit('climbing')