require icu.inc

LIC_FILES_CHKSUM = "file://../license.html;md5=8b139ac5b93769623bd343318048238c"

def icu_download_version(d):
    pvsplit = d.getVar('PV', True).split('.')
    return pvsplit[0] + "_" + pvsplit[1]

ICU_PV = "${@icu_download_version(d)}"

BASE_SRC_URI = "http://download.icu-project.org/files/icu4c/${PV}/icu4c-${ICU_PV}-src.tgz"
SRC_URI = "${BASE_SRC_URI} \
           file://icu-pkgdata-large-cmd.patch \
           file://icu-CVE-2014-8146-CVE-2014-8147.patch \
          "

SRC_URI_append_class-target = "\
           file://0001-Disable-LDFLAGSICUDT-for-Linux.patch \
          "

SRC_URI[md5sum] = "b73baa6fbdfef197608d1f69300919b9"
SRC_URI[sha256sum] = "6fa74fb5aac070c23eaba1711a7178fe582c59867484c5ec07c49002787a9a28"
