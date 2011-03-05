DESCRIPTION = "Meta package for building a installable toolchain"
LICENSE = "MIT"

PR = "r3"

LIC_FILES_CHKSUM = "file://${POKYBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${POKYBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit populate_sdk

CONFIG_SITE := "${@siteinfo_get_files(d)}"
