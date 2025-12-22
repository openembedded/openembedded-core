SUMMARY = "Library for building powerful interactive command lines in Python"
DESCRIPTION = "Measures the displayed width of unicode strings in a terminal"
HOMEPAGE = "https://github.com/jquast/wcwidth"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b15979c39a2543892fca8cd86b4b52cb"

SRC_URI[sha256sum] = "4d478375d31bc5395a3c55c40ccdf3354688364cd61c4f6adacaa9215d0b3605"

inherit pypi setuptools3 ptest-python-pytest

do_install_ptest:aapend() {
      install -d ${D}${PTEST_PATH}/bin
      cp -rf ${S}/bin/* ${D}${PTEST_PATH}/bin/
}

BBCLASSEXTEND = "native nativesdk"
