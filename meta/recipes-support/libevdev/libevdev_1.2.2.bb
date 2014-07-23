SUMMARY = "Wrapper library for evdev devices"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/libevdev/"
SECTION = "libs"

LICENSE = "MIT-X"
LIC_FILES_CHKSUM = "file://COPYING;md5=75aae0d38feea6fda97ca381cb9132eb \
                    file://libevdev/libevdev.h;endline=21;md5=7ff4f0b5113252c2f1a828e0bbad98d1"

SRC_URI = "http://www.freedesktop.org/software/libevdev/${BP}.tar.xz"
SRC_URI[md5sum] = "7c1ee9c2069489b2a25dfde6f8e2ff6a"
SRC_URI[sha256sum] = "860e9a1d5594393ff1f711cdeaf048efe354992019068408abbcfa4914ad6709"

inherit autotools pkgconfig
