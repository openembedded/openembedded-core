SRC_URI = "ftp://ftp.iana.org/tz/releases/tzdata${PV}.tar.gz;name=tzdata"

SRC_URI[tzdata.md5sum] = "f9f59425e55b21d2e52bb3a95fc17b8d"
SRC_URI[tzdata.sha256sum] = "5547d5b7a982f7f53fffe68cb2a13692553f591a17d58c8ef15b20679d82a270"

require tzdata.inc
