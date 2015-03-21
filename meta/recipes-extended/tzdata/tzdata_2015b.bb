SRC_URI = "ftp://ftp.iana.org/tz/releases/tzdata${PV}.tar.gz;name=tzdata"

SRC_URI[tzdata.md5sum] = "75571bb17c7b6be88a9f8872e45bc726"
SRC_URI[tzdata.sha256sum] = "556ac1a5b3a371adc1ad4e77138f78ddd7f8ddd7bc2b52545924598c7dc8ad62"

require tzdata.inc
