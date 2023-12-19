#import praw
import requests
import pandas as pd
import requests
import json
from datetime import datetime

# Reddit API credentials
client_id = 'ofmJ9OCy0zZcIT2mSBVedw'
client_secret = '5jcbxlK3673PtwDpETFAULLbPj_Rug'
user_agent = 'MyApi/0.0.1'

auth=requests.auth.HTTPBasicAuth(client_id,client_secret)#授权

with open('pw.txt','r') as f:
    pw = f.read()
    
#实际登陆
data={
    'grant_type':'password',
    'username':'Uze_loge',
    'password': pw,
    'User-Agent':'MyApi/0.0.1'
}

headers={'User-Agent':'MyApi/0.0.1'}

res=requests.post('https://www.reddit.com/api/v1/access_token',auth=auth,data=data,headers=headers)#对令牌的请求，给地址 https://www.reddit.com/api/v1/access_token发送请求

res.json() 
#print(res.json())
token=res.json()['access_token']
#print('the token is',token)
headers['authorization']=f'bearer {token}' #将令牌添加到标头中
#print(headers)
res=requests.get('https://oauth.reddit.com/api/v1/me',headers=headers)#访问端点
#res=requests.get('https://oauth.reddit.com/api/v1/me',headers=headers).json()#访问端点
print(res)
res=requests.get('https://oauth.reddit.com/r/FridaysForFuture',headers=headers)
res.json()
#print(res.json())

def get_subreddits_with_keyword(keyword):
    search_url = f"https://oauth.reddit.com/subreddits/search?q={keyword}"
    response = requests.get(search_url, headers=headers)

    if response.status_code != 200:
        print(f'Error: {response.status_code}')
        return []

    subreddits = response.json()['data']['children']
    return [subreddit['data']['display_name'] for subreddit in subreddits]

subreddits = get_subreddits_with_keyword("rock climbing")
print((subreddits))

# fetch posts data for each subreddit

df1 = pd.DataFrame()  # Create an empty DataFrame

# Reddit API endpoint URL
url = 'https://oauth.reddit.com/r/'
# Reddit API requires a User-Agent header for identification
headers = headers  # Make sure 'headers' variable is defined before this point

# Fetch data from each subreddit
# df2=pd.DataFrame()

df2_data = []

for subs in subreddits:
    url = f'https://oauth.reddit.com/r/{subs}'
    res = requests.get(url, headers=headers)

    # 检查响应状态码
    if res.status_code != 200:
        print(f"请求失败，状态码：{res.status_code}")
        continue  # 跳过当前子版块

    # 尝试获取数据，如果失败则打印错误信息
    try:
        posts = res.json()['data']['children']
        for post in posts:
            score = post['data']['score']
            upvote_ratio = post['data']['upvote_ratio']
            upvotes = int(round(score / upvote_ratio))
            downvotes = upvotes - score

            df2_data.append({
                'title': post['data']['title'],
                'upvotes': upvotes,
                'downvotes': downvotes,
                'num_comments': post['data']['num_comments'],
                'created_utc': post['data']['created_utc']
            })
    except KeyError as e:
        print(f"解析错误：{e}")
        print(res.json())  # 打印响应内容以帮助调试

df2 = pd.DataFrame(df2_data)
df2['created_utc'] = pd.to_datetime(df2['created_utc'], unit='s').dt.strftime('%d/%m/%Y')
print(df2)
json_data = json.dumps(df2_data, indent=4)

with open('data.json', 'w') as file:
    file.write(json_data)