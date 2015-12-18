SUMMARY = "libsecret is a library for storing and retrieving passwords and other secrets"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=23c2a5e0106b99d75238986559bb5fc6"

inherit gnomebase gtk-doc

DEPENDS = "glib-2.0 libgcrypt gettext-native intltool-native"

EXTRA_OECONF += "--disable-manpages"

SRC_URI[archive.md5sum] = "a21605644a64883ab685aec50d63253e"
SRC_URI[archive.sha256sum] = "f2bf1d0c5ab4640664f3e3c7ef6b086c180e50ff415720b5e22f96750dbf84c9"

# http://errors.yoctoproject.org/Errors/Details/20228/
ARM_INSTRUCTION_SET = "arm"
