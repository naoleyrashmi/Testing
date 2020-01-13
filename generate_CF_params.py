import os, sys
str = ''
for var in sys.argv[1].split():
    if var == "SeconderyStorageSpaceSize":
        str += var + ": " + os.getenv(var, '') + "\n"
    else:
        str += var + ": \"" + os.getenv(var, '') + "\"\n"
print str
f = open("params-" + ".yaml","w")
f.write(str)
f.close()