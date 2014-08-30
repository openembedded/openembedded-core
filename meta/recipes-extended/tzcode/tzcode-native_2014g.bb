# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2014g.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "f154679d48b724fe191efc19b55abd24"
SRC_URI[tzcode.sha256sum] = "df555fa4ad57cc7394812941d6d037b8c7ecea55e9916eaf4cd2adcb3403af7a"
SRC_URI[tzdata.md5sum] = "f9f59425e55b21d2e52bb3a95fc17b8d"
SRC_URI[tzdata.sha256sum] = "5547d5b7a982f7f53fffe68cb2a13692553f591a17d58c8ef15b20679d82a270"

require tzcode-native.inc
