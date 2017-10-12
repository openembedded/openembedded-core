SUMMARY = "Perl module for supporting simple generic namespaces"
HOMEPAGE = "http://veillard.com/XML/"
DESCRIPTION = "XML::NamespaceSupport offers a simple way to process namespace-based XML names. \
                It also helps maintain a prefix-to-namespace URI map, and provides a number of \
                basic checks. "

SECTION = "libs"
LICENSE = "Artistic-1.0 | GPL-1.0+"
PR = "r3"

LIC_FILES_CHKSUM = "file://META.yml;beginline=22;endline=22;md5=9ca1a4a941496e7feedac72c4fb8b137"

# the upstream project complicate a bit url generartion on point release, using a underscore
# instead of a point on URL, i.e 1.12_9 instead of 1.12.9
python () {
    baseurl = "http://search.cpan.org/CPAN/authors/id/P/PE/PERIGRIN/XML-NamespaceSupport"
    pv = d.getVar('PV')
    pvsplit = pv.split('.')

    if len(pvsplit) != 3:
        d.setVar('SRC_URI', "%s-%s.tar.gz" % (baseurl, pv))
        d.setVar('S', "${WORKDIR}/XML-NamespaceSupport-${PV}")
    else:
        pvx, pvy, pvz = pvsplit
        d.setVar('SRC_URI', "%s-%s.%s_%s.tar.gz" % (baseurl, pvx, pvy, pvz))
        d.setVar('S', "${WORKDIR}/XML-NamespaceSupport-%s.%s_%s" % (pvx, pvy, pvz))
}

SRC_URI[md5sum] = "165927a311fb640961b28607035beab8"
SRC_URI[sha256sum] = "2e84a057f0a8c845a612d212742cb94fca4fc8a433150b5721bd448f77d1e4a9"

inherit cpan

BBCLASSEXTEND="native"

