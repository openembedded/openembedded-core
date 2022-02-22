SUMMARY = "The PyPA recommended tool for installing Python packages"
HOMEPAGE = "https://pypi.org/project/pip"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=63ec52baf95163b597008bb46db68030"

inherit pypi setuptools_build_meta

DEPENDS += "python3 python3-setuptools-native"

# To avoid a dependency loop; we bootstrap -native
DEPENDS:remove:class-native = "python3-pip-native"
DEPENDS:append:class-native = " unzip-native"

SRC_URI += "file://0001-change-shebang-to-python3.patch"

SRC_URI[sha256sum] = "f29d589df8c8ab99c060e68ad294c4a9ed896624f6368c5349d70aa581b333d0"

PYPA_WHEEL ?= "${B}/dist/${PYPI_PACKAGE}-${PV}-*.whl"

do_install:class-native() {
    # Bootstrap to prevent dependency loop in python3-pip-native
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
    unzip -d ${D}${PYTHON_SITEPACKAGES_DIR} ${PYPA_WHEEL} || \
    bbfatal_log "Failed to unzip wheel: ${PYPA_WHEEL}. Check the logs."
}

do_install:append() {
    # Install as pip3 and leave pip2 as default
    if [ -e ${D}/${bindir}/pip ]; then
        rm ${D}/${bindir}/pip
    fi
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
