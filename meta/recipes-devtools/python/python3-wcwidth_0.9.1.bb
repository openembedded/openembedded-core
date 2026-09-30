SUMMARY = "Library for building powerful interactive command lines in Python"
DESCRIPTION = "Measures the displayed width of unicode strings in a terminal"
HOMEPAGE = "https://github.com/jquast/wcwidth"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b15979c39a2543892fca8cd86b4b52cb"

SRC_URI[sha256sum] = "5823209b0d43af322ce698c689380d7c15ca31fa8e6e3be8459f27031bef0af5"

inherit pypi python_hatchling ptest-python-pytest

do_install_ptest:append() {
      cp ${S}/pyproject.toml ${S}/setup.py ${D}${PTEST_PATH}/
      cp -r ${S}/libwcwidth ${D}${PTEST_PATH}/libwcwidth

      # Remove pre-release tests, require authentication with github
      rm ${D}${PTEST_PATH}/tests/test_check_release.py
}

BBCLASSEXTEND = "native nativesdk"
