require go-common.inc

GOMINOR = "3"
GO_BASEVERSION = "1.8"
PV .= ".${GOMINOR}"
FILESEXTRAPATHS_prepend := "${FILE_DIRNAME}/go-${GO_BASEVERSION}:"

LIC_FILES_CHKSUM = "file://LICENSE;md5=5d4950ecb7b26d2c5e4e7b4e0dd74707"

SRC_URI += "\
       file://armhf-elf-header.patch \
       file://syslog.patch \
       file://fix-target-cc-for-build.patch \
       file://fix-cc-handling.patch \
       file://split-host-and-target-build.patch \
       file://gotooldir.patch \
"
SRC_URI[md5sum] = "64e9380e07bba907e26a00cf5fcbe77e"
SRC_URI[sha256sum] = "5f5dea2447e7dcfdc50fa6b94c512e58bfba5673c039259fd843f68829d99fa6"
