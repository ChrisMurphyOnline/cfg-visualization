def main():
    #filename = "small.csv"
    filename = "/usr/local/doc/climatewatchdata.csv"
    f=open(filename,"r")
    file_data=f.readlines()

    fix_list(file_data)

    data=get_data(file_data)

    main_menu(data)

###########################################
def fix_list(file_data):
    """ 
    Fixes a list of strings: strip off any trailing or leading white
    spaces from each string in the list 

    file_data: (lst) list of strings of the data

    returns: (none) modifies original list
    """

    for i in range(len(file_data)):
        file_data[i]=file_data[i].strip()

def get_data(file_data):
    """
    Takes a list of comma separated strings, and convert each element
    into an object

    file_data: (lst) list of strings of the data
    
    returns: (lst) list of Country objects
    """
    countries=[]
    number_of_countries=0
    for line in file_data:
        parts=line.split(",")
        name=parts[0]
        co2_vals=[float(parts[1]),float(parts[2]), float(parts[3]),
        float(parts[4]), float(parts[5])]
        pop_1960=float(parts[6])
        pop_2020=float(parts[7])
        gdp_1960=float(parts[8])
        gdp_2020=float(parts[9])
        next_country=Country(name, co2_vals, pop_1960, pop_2020, 
        gdp_1960, gdp_2020)
        countries.append(next_country)
        number_of_countries=number_of_countries + 1
    print("There are data for %d countries in this file"\
        %(number_of_countries))

    return countries

def main_menu(data):
    """
    Prints out the 5 menu options and prompts user to select a valid option.
    Based on user option, display requested information.

    data: (lst) list of countries and their info as objects

    returns: none
    """
    option=0
    while option!=5:
        print()
        print("========  Menu Options: ========")
        print("1. The n largest C02 producing countries in given year")
        print("   (listed in order of largest to smallest CO2 levels)")
        print("2. The CO2 values for n countries with largest GDP in" +\
              " given year")
        print("   (listed in order of largest to smallest GDP)")
        print("3. The n largest CO2/population producing countries" +\
              " in a given year")
        print("   (listed in order of largest to smallest CO2/population" +\
              " levels")
        print("4. Print a country's info")
        print("5. quit")
        print()

        option=get_value_between("Select a menu option", 1, 5)
        if option==5:
            print("bye bye")
            exit()
        if option==1:
            print()
            n1=get_value_between("Pick a value for n ", 1, 193)
            print()
            get_year1=get_value_in_set(\
                "Enter a year (one of 1960, 1980, 2000, 2020, 2022): ", \
                    [1960, 1980, 2000, 2020, 2022])
            option_one(n1, get_year1, data)
        if option==2:
            print()
            n2=get_value_between("Pick a value for n ", 1, 193)
            print()
            get_year2=get_value_in_set("Enter a year (one of 1960 or 2020): ",\
                [1960, 2020])
            option_two(n2, get_year2, data)
        if option==3:
            print()
            n3=get_value_between("Pick a value for n ", 1, 193)
            print()
            get_year3=get_value_in_set("Enter a year (one of 1960 or 2020): ",\
                [1960, 2020])
            option_3(data)# option_three(n3, get_year3, data)
        if option==4:
            print()
            country=(input("Enter the name of a country: "))
            index=option_four(data, country)
            print()
            if index==-1:
                print("Sorry, %s is not in the database" %(country))
            else:
                print(data[index])

def get_value_between(prompt, low, high):
    """
    Prints the prompt and ask user to input a value between given parameter.

    prompt: (str) str containing prompt for the user

    low: (int) smallest possible option

    high: (int) highest possible option

    return: (int) return user option
    """
    print(prompt)
    option=int(input("Enter a value between %d and %d: " %(low, high)))
    while (option<low or option>high):
        print("  %d is not a valid choice, try again" %(option))
        option=int(input("Enter a value between %d and %d: " %(low, high))) 

    return option  

def get_value_in_set(prompt, list):
    """
    Prompts user to input a value in the list.

    prompt: (str) str containing prompt for the user

    list: (list) list containing possible options user can choose from

    return: (int) return user option
    """
    option=int(input(prompt))
    while ((option in list)==False):
        print("  %d is not a valid value, try again" %(option))
        option=int(input(prompt))
    
    return int(option)

def option_one(n, year, data):
    """
    Uses selection sort to sort the list of Country objects fromn highest
    to lowest CO2 emissions in a given year. Print out information of n 
    countries with highest emissions.

    n: (int) n number of countries' information to print out

    year: (int) the year that the user wants to look into

    data: (lst) list of countries and their info as objects

    returns: none
    """
    for i in range(len(data)-1):
        idx=find_largest_CO2(data, i, len(data), year)
        swap(data, i, idx)

    print()
    print()
    print("%d       Emissions in Mt         Country" %(year))
    print("-------------------------------------------------------")
    missing=0
    for i in range(n):
        if data[i].getCO2(year)!=-1:
            print("%3d.      %15.6f        %-20s" %(i+1, data[i].getCO2(year), \
                data[i].getName() )) 
        if (data[i].getCO2(year)==-1):
            missing=missing+1
    if missing!=0:
        print()
        print("%d of the top %d C02 emitters do not have CO2 data for %d" \
            %(missing, n, year))

def find_largest_CO2(data, start, stop, year):
    """
    Finds the index of the country with the highest CO2 emission.

    data: (lst) list of countries and their info as objects

    start: (int) index of the first element of the unsorted list

    stop: (int) number of elements in the list of data

    year: (int) the year that the user wants to look into

    returns: (int) returns index of the country with the highest CO2 emission
    """
    max=data[start].getCO2(year)
    idx=start
    for i in range(start+1, stop):
        if (data[i].getCO2(year)>max) and (data[i].getCO2(year)!=-1):
            max=data[i].getCO2(year)
            idx=i
    return idx

def swap(data, i, j):
    """
    Swaps the objects in data list based on provided index.

    i: (int) index of the first element of the unsorted list

    j: (int) index of the desired element to be swapped with i

    retunrs: none
    """
    tmp=data[i]
    data[i]=data[j]
    data[j]=tmp

def option_two(n, year, data):
    """
    Usea selection sort to sort the list of Country objects from highest to 
    lowest GDP in a given year. List the CO2 and GDP information for the n 
    countries with the highest GDP.

    n: (int) n number of countries' information to print out

    year: (int) the year that the user wants to look into

    data: (lst) list of countries and their info as objects

    returns: none
    """
    for i in range(len(data)-1):
        idx=find_largest_GDP(data, i, len(data), year)
        swap(data, i, idx)
    print()
    print()
    print("%d         CO2(Mt)           GDP(B)         Country" %(year))
    print("------------------------------------------------------------")
    missing=0
    for i in range(n):
        if (data[i].getGDP(year)!=-1 and data[i].getCO2(year)!=-1):
            print("%3d.     %12.6f      %12.6f      %-20s" %(i+1,\
                data[i].getCO2(year), data[i].getGDP(year), data[i].getName()))
        else:
            missing=missing+1
    if missing!=0:
        print()
        print("%d of the top %d have missing CO2 or GDP data for %d" \
            %(missing, n, year))

def find_largest_GDP(data, start, stop, year):
    """
    Finds the index of the country with the highest GDP.

    data: (lst) list of countries and their info as objects

    start: (int) index of the first element of the unsorted list

    stop: (int) number of elements in the list of data

    year: (int) the year that the user wants to look into

    returns: (int) returns index of the country with the highest GDP
    """
    max=data[start].getGDP(year)
    idx=start
    for i in range(start+1, stop):
        if (data[i].getGDP(year)>max) and (data[i].getGDP(year)!=-1):
            max=data[i].getGDP(year)
            idx=i
    return idx


def option_3(data):
    n = get_value_between("Enter a value between 1 and 193: ", 1, 193)
    year = get_value_in_set("Enter a year (one of 1960 or 2020): ", [1960, 2020])
    sort_option_3(data, year)
    nmissing = 0
    for i in range(n):
        if i == 78:
            print(data[i])
        if data[i].getCO2(year) != -1 and data[i].getPopulation(year) != -1:
            print("%d. %.6f %.6f %.6f %s" % (i+1, data[i].getCO2(year)/data[i].getPopulation(year), data[i].getCO2(year), data[i].getPopulation(year), data[i].getName()))
        else:
            nmissing = nmissing + 1
    print("%d of the top %d have missing CO2 or population data for %d" % (nmissing, n, year))
    for i in range(n):
        print("%d. %.6f %.6f %.6f %s" % (i+1, data[i].getCO2(year)/data[i].getPopulation(year), data[i].getCO2(year), data[i].getPopulation(year), data[i].getName()))



def sort_option_3(lst, year):
    for i in range(len(lst)):
        max_index = i
        for j in range(i, len(lst)):
            if lst[j].getCO2(year)/lst[j].getPopulation(year) > lst[max_index].getCO2(year)/lst[max_index].getPopulation(year):
                max_index = j
        swap(lst, i, max_index)


def option_three(n, year, data):
    """
    Uses selection sort to sort the list of Country objects from highest to
    lowest CO2/capita in a given year. Print out information (CO2 per capita,
    CO2 emissions, and population) about the top n CO2/capita producing 
    countries. 

    n: (int) n number of countries' information to print out

    year: (int) the year that the user wants to look into

    data: (lst) list of countries and their info as objects

    returns: none
    """
    for i in range(len(data)-1):
        idx=find_largest_CO2perPop(data, i, len(data), year)
        swap(data, i, idx)
    print("len(data) = " + str(len(data)))
    print()
    print("%d   CO2(Mt)/M people     CO2(Mt)       Population(M)" %(year) +\
          "    Country")
    print("---------------------------------------------------------------" +\
          "-------")
    missing=0
    for i in range(n):
        if i == 130:
            print(data[i])
        if(data[i].getCO2(year)!=-1) and (data[i].getPopulation(year)!=-1):
            print("%3d.   %12.6f     %12.6f     %12.6f     %-20s" %(i+1,\
                (data[i].getCO2(year)/data[i].getPopulation(year)),\
                      data[i].getCO2(year), data[i].getPopulation(year),\
                        data[i].getName()))
        else:
            missing=missing+1
    if missing!=0:
        print()
        print("%d of the top %d have missing CO2 or population data for %d" \
            %(missing, n, year))

def find_largest_CO2perPop(data, start, stop, year):
    """
    Finds the index of the country with the highest CO2/capita.

    data: (lst) list of countries and their info as objects

    start: (int) index of the first element of the unsorted list

    stop: (int) number of elements in the list of data

    year: (int) the year that the user wants to look into

    returns: (int) returns index of the country with the highest GDP
    """
    max=(data[start].getCO2(year))/(data[start].getPopulation(year))
    idx=start
    for i in range(start+1, stop):
        if ((data[i].getCO2(year))/(data[i].getPopulation(year))>max) \
            and (data[i].getCO2(year)!=-1) \
                and (data[i].getPopulation(year)!=-1):
            max=(data[i].getCO2(year))/(data[i].getPopulation(year))
            idx=i
    return idx

def option_four(data, country):
    """
    Uses selection sort to order the list of Country objects in acending
    alphabetical order based on country names. Uses binary search to find 
    the index of the wanted country.

    data: (lst) list of countries and their info as objects

    country: (str) user inputted country that they want to search for

    return: (int) returns index of wanted country if found, if not, return -1
    """
    for i in range(len(data)-1):
        idx=find_smallest_country(data, i, len(data))
        swap(data, i, idx)

    low = 0
    high = len(data) - 1
    while high >= low:
        mid = (low + high) // 2
        if (data[mid].getName() == country):
            return mid
        elif (data[mid].getName() > country):
            high = mid - 1
        else: 
            low = mid + 1
    return -1 

def find_smallest_country(data, start, stop):
    """
    Finds the index of the country most forefront in the alphabet. 

    data: (lst) list of countries and their info as objects

    start: (int) index of the first element of the unsorted list

    stop: (int) number of elements in the list of data

    returns: none
    """
    min=data[start].getName()
    idx=start
    for i in range(start+1, stop):
        if (data[i].getName()<min):
            min=data[i].getName()
            idx=i
    return idx
main()
