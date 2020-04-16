#!/usr/bin/env python
# coding: utf-8

# In[12]:


import string

def generate_password(words):
    #первая буква
    letter = words[2][1]
    if (letter == 'z'):
        letter_1 = 'a'
    else:
        letter_1 = chr(ord(letter) + 1)
    
    #вторая буква  
    letter = words[1][1]
    if (letter == 'a'):
        letter_2 = 'z'
    else:
        letter_2 = chr(ord(letter) - 1)
    
    #третья буква
    if (len(words[2]) % 2 == 1):
        if (words[2][-1]=='z'):
            letter_3 = 'a'
        else:
            letter_3 = chr(ord(words[2][-1]) + 1)
    else:
        if (words[2][2] == 'a'):
            letter_3 = 'z'
        else:
            letter_3 = chr(ord(words[2][2]) - 1)
    
    #четвертая буква
    import string
    d = dict(enumerate(string.ascii_lowercase, 1))
    
    if ((len(words[0]) + len(words[1]) + 3) > 26):
        letter_4 = d[(len(words[0]) + len(words[1]) + 3) % 26]
    else:
        letter_4 = d[(len(words[0]) + len(words[1]) + 3)]
        
    password = letter_1 + letter_2 + letter_3 + letter_4 
    return password


# In[15]:


#генерация пароля
strings = ['pipers', 'hauls', 'polios']
print(generate_password(strings))

#авторизация
import getpass

password = getpass.getpass()
if (password == generate_password(strings)):
    print('OK!')
else:
    print('BOO!')


# In[29]:


#2ое задание

#вычисление длины пароля

p = 10 ** (-4)
v = 3 * 60 * 24 * 15
t = 15

s = (v * t)/p

print(s)

#мощность 64
a = 64
# a в шестой степени больше s, a в пятой меньше. значит длина пароля - 6
print(a ** 6)


import random

chars = 'абвгдеёжзийклмнопрстуфхцчшщъьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЬЭЮЯ'
number = int(input('количество паролей?'+ "\n"))

length = int(input('длина пароля?'+ "\n"))
for n in range(number):
    password =''
    for i in range(length):
        password += random.choice(chars)
    print(password)


# In[28]:





# In[ ]:




