require cmake.inc
inherit native

# This was need to keep version consistent - will be removed once 2.8.5 is released
SRC_URI = "http://www.cmake.org/files/v${CMAKE_MAJOR_VERSION}/cmake-2.8.5-rc3.tar.gz \
           file://support-oe-qt4-tools-names.patch"
S = "${WORKDIR}/cmake-2.8.5-rc3"

PR = "${INC_PR}.0"

SRC_URI[md5sum] = "2d8018f8fa4c499e2c5b288d71660cba"
SRC_URI[sha256sum] = "2987befc451f6404ea93bb99f00a38b80724fb655f121fed3bb0a08b65a771c8"
