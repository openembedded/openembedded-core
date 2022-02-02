SUMMARY = "The PyPA recommended tool for installing Python packages"
HOMEPAGE = "https://pypi.org/project/pip"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=63ec52baf95163b597008bb46db68030"

DEPENDS += "python3 python3-setuptools-native"

inherit pypi setuptools3

SRC_URI += "file://0001-change-shebang-to-python3.patch"

SRC_URI[sha256sum] = "27b4b70c34ec35f77947f777070d8331adbb1e444842e98e7150c288dc0caea4"

do_install:append() {
    # Install as pip3 and leave pip2 as default
    rm ${D}/${bindir}/pip
}

RDEPENDS:${PN} = "\
  python3-compile \
  python3-io \
  python3-html \
  python3-json \
  python3-multiprocessing \
  python3-netserver \
  python3-setuptools \
  python3-unixadmin \
  python3-xmlrpc \
  python3-pickle \
"

BBCLASSEXTEND = "native nativesdk"
