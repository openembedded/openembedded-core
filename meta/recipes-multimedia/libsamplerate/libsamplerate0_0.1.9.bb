SUMMARY = "Audio Sample Rate Conversion library"
HOMEPAGE = "http://www.mega-nerd.com/SRC/"
SECTION = "libs"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=336d6faf40fb600bafb0061f4052f1f4 \
                    file://src/samplerate.c;beginline=1;endline=7;md5=5b6982a8c2811c7312c13cccbf55f55e"
DEPENDS = "flac libsndfile1"
PR = "r1"

SRC_URI = "http://www.mega-nerd.com/SRC/libsamplerate-${PV}.tar.gz"

SRC_URI[md5sum] = "2b78ae9fe63b36b9fbb6267fad93f259"
SRC_URI[sha256sum] = "0a7eb168e2f21353fb6d84da152e4512126f7dc48ccb0be80578c565413444c1"

UPSTREAM_CHECK_URI = "http://www.mega-nerd.com/SRC/download.html"

S = "${WORKDIR}/libsamplerate-${PV}"

inherit autotools pkgconfig

PACKAGECONFIG[fftw] = ",--disable-fftw,fftw"
