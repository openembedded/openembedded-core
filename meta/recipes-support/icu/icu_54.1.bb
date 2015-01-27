require icu.inc

LIC_FILES_CHKSUM = "file://../license.html;md5=9890f5ff4ed056a0c2fa84848b9b6066"

def icu_download_version(d):
    pvsplit = d.getVar('PV', True).split('.')
    return pvsplit[0] + "_" + pvsplit[1]

ICU_PV = "${@icu_download_version(d)}"

BASE_SRC_URI = "http://download.icu-project.org/files/icu4c/${PV}/icu4c-${ICU_PV}-src.tgz"
SRC_URI = "${BASE_SRC_URI} \
           file://icu-pkgdata-large-cmd.patch \
          "

SRC_URI_append_class-target = "\
           file://0001-Disable-LDFLAGSICUDT-for-Linux.patch \
          "
SRC_URI[md5sum] = "e844caed8f2ca24c088505b0d6271bc0"
SRC_URI[sha256sum] = "d42bc9a8ca6a91c55eb0925c279f49e5b508d51ef26ac9850d9be55de5bb8ab3"

