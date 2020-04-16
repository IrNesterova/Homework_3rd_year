#!/usr/bin/env python
# coding: utf-8

# In[4]:


import math
import sys

def route_encrypt(plain_text="", step_size=4):
	"""
		Takes plain text as input and produces encrypted text as output
		Implements the route cipher technique
	"""

	idx = 0
	matrix_representation = []
	encrypted_text = ""

	# create a matrix from plain text with width = step_size
	for i in range(math.ceil(len(plain_text)/step_size)):
		matrix_row = []
		for j in range(step_size):
			if i*step_size+j < len(plain_text):
				matrix_row.append(plain_text[i*step_size+j])
			else:
				matrix_row.append("-")
		matrix_representation.append(matrix_row)

	matrix_width = len(matrix_representation[0])
	matrix_height = len(matrix_representation)
	allowed_depth = 0

	if matrix_width < matrix_height:
		allowed_depth = matrix_width // 2
	else:
		allowed_depth = matrix_height // 2


	# Here "i" denotes the depth we're into the matrix
	# Here we read the normal matrix in a spiral form starting from top right corner
	for i in range(allowed_depth):

		# Going down on right side
		for j in range(i, matrix_height-i-1):
			encrypted_text += matrix_representation[j][matrix_width-i-1]

		# Going left on the bottom side
		for j in range(matrix_width-i-1, i, -1):
			encrypted_text += matrix_representation[matrix_height-i-1][j]

		# Going up on the left side
		for j in range(matrix_height-i-1, i, -1):
			encrypted_text += matrix_representation[j][i]
			
		# Going right on the top side
		for j in range(i, matrix_width-i-1):
			encrypted_text += matrix_representation[i][j]


	return encrypted_text

def route_decrypt(cipher_text="", step_size=4):
	"""
		Takes Ciphered text as input and produces plain text as output
		Implements the route cipher technique's opposite
	"""

	idx = 0
	plain_text = ""

	matrix_width = step_size
	matrix_height = math.ceil(len(cipher_text)/step_size)

	if matrix_width < matrix_height:
		allowed_depth = matrix_width // 2
	else:
		allowed_depth = matrix_height // 2

	plain_text_matrix = [[0 for i in range(matrix_width)] for j in range(matrix_height)]

	# Here "i" denotes the depth we're into the matrix
	# Here we are recreating the "matrix_representation" matrix present in the encrypt function
	# We do so by performing the exact opposite of encryption step
	# We read the encrypted text in in order and add it to the matrix in a spiral form
	for i in range(allowed_depth):

		# Going down on right side
		for j in range(i, matrix_height-i-1):
			plain_text_matrix[j][matrix_width-i-1] = cipher_text[idx]
			idx += 1

		# Going left on the bottom side
		for j in range(matrix_width-i-1, i, -1):
			plain_text_matrix[matrix_height-i-1][j] = cipher_text[idx]
			idx += 1

		# Going up on the left side
		for j in range(matrix_height-i-1, i, -1):
			plain_text_matrix[j][i] = cipher_text[idx]
			idx += 1
			
		# Going right on the top side
		for j in range(i, matrix_width-i-1):
			plain_text_matrix[i][j] = cipher_text[idx]
			idx += 1

	# reconstruct the message by reading the plain matrix row by row
	for i in range(matrix_height):
		for j in range(matrix_width):
			plain_text += plain_text_matrix[i][j]

	return plain_text



cipher = route_encrypt(plain_text="To be or not to Be that is the Question")
print(cipher)

deciphered_text = route_decrypt(cipher_text=cipher)
print(deciphered_text)


# In[ ]:




