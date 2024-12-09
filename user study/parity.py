def parity(message, type):
    for c in message:
        if c != '0' and c != '1':
            return -1 #ignore this maybe?
    
    if type != 'even' and type != 'odd':
        return -1 #ignore this too
    
    count = 0
    for c in message:
        if c == '1':
            count = count + 1
    
    if type == 'even':
        if count % 2 == 0: #number of ones are already even here, so you can just return 0
            return 0
        else:
            return 1
    else:
        #if type equals odd: aka we want the 0's to be odd
        
        if count % 2 == 1 and count!=2:#this means that the number of ones are odd
            
            return 0
        else:
            return 1

def tests():
    assert parity('01a', 'even') == -1

    assert parity('01', 'cow') == -1

    assert parity('0110', 'even') == 0

    assert parity('011', 'even') == 0

    assert parity('0111', 'even') == 1

    assert parity('00111', 'even') == 1

    assert parity('01', 'odd') == 0 # error is here, supposed to return 1, because the number
    #of 0's is already odd 
    

    assert parity('011', 'odd') == 1

    assert parity('01101', 'odd') == 0

    assert parity('', 'odd') == 1

    print("All tests passed!")

if __name__ == "__main__":
    tests()