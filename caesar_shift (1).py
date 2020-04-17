#!/usr/bin/env python
# coding: utf-8

# In[12]:


llst = ['а','б','в','г','д','е','ж','з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х','ц','ч','ш','щ','ъ','ы','ь','э','ю','я']
blst = ['А','Б','В','Г','Д','Е','Ж','З','И','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х','Ц','Ч','Ш','Щ','Ъ','Ы','Ь','Э','Ю','Я']
 
def encryptCaesar(msg, shift):
    ret = ""
    for x in msg:
        if x in llst:
            ind = llst.index(x)%len(llst)
            ret += llst[(ind+shift)%len(llst)]
        elif x in blst:
            ind = blst.index(x)%len(llst)
            ret += blst[(ind+shift)%len(llst)]
        else:
            ret += x
    return ret
 
def decryptCaesar(msg, shift):
    ret = ""
    for x in msg:
        if x in llst:
            ind = llst.index(x)
            ret += llst[ind-shift]
        elif x in blst:
            ind = blst.index(x)
            ret += blst[ind-shift]
        else:
            ret += x
    return ret
 
print(encryptCaesar("Мне было тогда лет двадцать пять, — начал H. H., — дела давно минувших дней, как видите.", 8))
print(decryptCaesar("Фхн йгуц ъцлми унъ мкимюиъд чзъд, — хияиу H. H., — мнуи микхц фрхыкарэ мхнс, тит крмрън.",8))


# In[1]:


import re
import math
import gmpy2

def encode(plain_text, a, b):
    m = 26

    if math.gcd(a, m) != 1:
        raise ValueError("Error: a and m must be coprime.")

    output = ""
    plain_text = re.sub(r"[^a-z0-9]", "", plain_text.lower())

    # Encoding
    for ch in plain_text:
        x = ord(ch) - 97
        if x in range (0, 26):    # If it's a letter...
            code = ((a * x) + b) % m
            output += chr(code + 97)
        else:                     # If it's a number...
            output += ch

    # Let's split my output into blocks of 5 characters
    output = re.sub (r"([\w]{5})", r"\1 ", output)   

    return output.rstrip()


def decode(ciphered_text, a, b):
    m = 26

    if math.gcd(a, m) != 1:
        raise ValueError("Error: a and m must be coprime.")

    ciphered_text = ciphered_text.lower()
    ciphered_text = str.replace(ciphered_text," ","")
    output = ""

    MMI = gmpy2.invert(a, m)

    for ch in ciphered_text:
        y = ord(ch) - 97
        if y in range (0,26):
            code = (MMI * (y - b)) % m
            output += chr(code + 97)
        else:
            output += ch
    
    return output


# In[2]:


text = 'Hbp fntt ecv kbr dqk ecv tenzj'
decode(text,11,3)


# In[ ]:





# In[ ]:





# In[ ]:




