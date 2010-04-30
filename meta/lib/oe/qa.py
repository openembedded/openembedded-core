class ELFFile:
    EI_NIDENT = 16

    EI_CLASS      = 4
    EI_DATA       = 5
    EI_VERSION    = 6
    EI_OSABI      = 7
    EI_ABIVERSION = 8

    # possible values for EI_CLASS
    ELFCLASSNONE = 0
    ELFCLASS32   = 1
    ELFCLASS64   = 2

    # possible value for EI_VERSION
    EV_CURRENT   = 1

    # possible values for EI_DATA
    ELFDATANONE  = 0
    ELFDATA2LSB  = 1
    ELFDATA2MSB  = 2

    def my_assert(self, expectation, result):
        if not expectation == result:
            #print "'%x','%x' %s" % (ord(expectation), ord(result), self.name)
            raise Exception("This does not work as expected")

    def __init__(self, name, bits32):
        self.name = name
        self.bits32 = bits32

    def open(self):
        self.file = file(self.name, "r")
        self.data = self.file.read(ELFFile.EI_NIDENT+4)

        self.my_assert(len(self.data), ELFFile.EI_NIDENT+4)
        self.my_assert(self.data[0], chr(0x7f) )
        self.my_assert(self.data[1], 'E')
        self.my_assert(self.data[2], 'L')
        self.my_assert(self.data[3], 'F')
        if self.bits32 :
            self.my_assert(self.data[ELFFile.EI_CLASS], chr(ELFFile.ELFCLASS32))
        else:
            self.my_assert(self.data[ELFFile.EI_CLASS], chr(ELFFile.ELFCLASS64))
        self.my_assert(self.data[ELFFile.EI_VERSION], chr(ELFFile.EV_CURRENT) )

        self.sex = self.data[ELFFile.EI_DATA]
        if self.sex == chr(ELFFile.ELFDATANONE):
            raise Exception("self.sex == ELFDATANONE")
        elif self.sex == chr(ELFFile.ELFDATA2LSB):
            self.sex = "<"
        elif self.sex == chr(ELFFile.ELFDATA2MSB):
            self.sex = ">"
        else:
            raise Exception("Unknown self.sex")

    def osAbi(self):
        return ord(self.data[ELFFile.EI_OSABI])

    def abiVersion(self):
        return ord(self.data[ELFFile.EI_ABIVERSION])

    def isLittleEndian(self):
        return self.sex == "<"

    def isBigEngian(self):
        return self.sex == ">"

    def machine(self):
        """
        We know the sex stored in self.sex and we
        know the position
        """
        import struct
        (a,) = struct.unpack(self.sex+"H", self.data[18:20])
        return a
