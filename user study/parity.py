def parity(message, type):
    for c in message:
        if c != '0' and c != '1':
            return -1
    
    if type != 'even' and type != 'odd':
        return -1
    
    count = 0
    for c in message:
        if c == '1':
            count = count + 1
    
    if type == 'even':
        if count % 2 == 0:
            return 0
        else:
            return 1
    else:
        if count % 2 == 1:
            return 1
        else:
            return 0

def main():
    assert parity('01a', 'even') == -1

    assert parity('01', 'cow') == -1

    assert parity('0110', 'even') == 0

    assert parity('011', 'even') == 0

    assert parity('0111', 'even') == 1

    assert parity('00111', 'even') == 1

    assert parity('01', 'odd') == 0

    assert parity('011', 'odd') == 1

    assert parity('01101', 'odd') == 0

    assert parity('', 'odd') == 1

    print("All tests passed!")

if __name__ == "__main__":
    main()