DESCRIPTION = "Meta package for building a installable toolchain"
LICENSE = "MIT"

PR = "r4"

LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

IMAGETEST ?= "dummy"
inherit populate_sdk imagetest-${IMAGETEST}

CONFIG_SITE := "${@siteinfo_get_files(d)}"
