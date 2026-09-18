SUMMARY = "Library for building powerful interactive command lines in Python"
DESCRIPTION = "Measures the displayed width of unicode strings in a terminal"
HOMEPAGE = "https://github.com/jquast/wcwidth"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b15979c39a2543892fca8cd86b4b52cb"

SRC_URI[sha256sum] = "2dae09efa25253ae2874188e86d6861af3b1652aef4118cdf3f0bda288a957fb"

inherit pypi python_hatchling ptest-python-pytest

do_install_ptest:append() {
      cp ${S}/pyproject.toml ${D}${PTEST_PATH}/
}

BBCLASSEXTEND = "native nativesdk"
