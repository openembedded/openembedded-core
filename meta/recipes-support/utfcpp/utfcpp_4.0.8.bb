SUMMARY = " UTF-8 with C++ in a Portable Way"
HOMEPAGE = "https://github.com/nemtrif/utfcpp"

LICENSE = "BSL-1.0 & MIT"

LIC_FILES_CHKSUM = "file://LICENSE;md5=e4224ccaecb14d942c71d31bef20d78c \
                    file://tests/ftest.h;endline=25;md5=d33c6488d3b003723a5f17ac984db030"

SRC_URI = "git://github.com/nemtrif/utfcpp;protocol=https;branch=master;tag=v${PV}"

SRCREV = "f9319195dfddf369f68f18e7c0039b3f351797fd"

inherit cmake

FILES:${PN}-dev += "${datadir}/utf8cpp/cmake"
