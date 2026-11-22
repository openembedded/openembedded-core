SUMMARY = "Library for building powerful interactive command lines in Python"
DESCRIPTION = "Measures the displayed width of unicode strings in a terminal"
HOMEPAGE = "https://github.com/jquast/wcwidth"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b15979c39a2543892fca8cd86b4b52cb"

SRC_URI[sha256sum] = "713534e2ecb16e0fc39f19e0a5dda997da76d010953e9e8f803e7971f8accd80"

inherit pypi python_hatchling ptest-python-pytest

do_install_ptest:append() {
      cp ${S}/pyproject.toml ${S}/setup.py ${D}${PTEST_PATH}/
      cp -r ${S}/libwcwidth ${D}${PTEST_PATH}/libwcwidth

      # Remove pre-release tests, require authentication with github
      rm ${D}${PTEST_PATH}/tests/test_check_release.py
}

BBCLASSEXTEND = "native nativesdk"
