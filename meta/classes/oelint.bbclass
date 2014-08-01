addtask lint before do_fetch
do_lint[nostamp] = "1"
python do_lint() {
    pkgname = d.getVar("PN", True)

    ##############################
    # Test that DESCRIPTION exists
    #
    description = d.getVar("DESCRIPTION")
    if description[1:10] == '{SUMMARY}':
        bb.warn("%s: DESCRIPTION is not set" % pkgname)


    ##############################
    # Test that HOMEPAGE exists
    #
    homepage = d.getVar("HOMEPAGE")
    if homepage == '':
        bb.warn("%s: HOMEPAGE is not set" % pkgname)
    elif not homepage.startswith("http://") and not homepage.startswith("https://"):
        bb.warn("%s: HOMEPAGE doesn't start with http:// or https://" % pkgname)


    ##############################
    # Test for valid SECTION
    #
    section = d.getVar("SECTION")
    if section == '':
        bb.warn("%s: SECTION is not set" % pkgname)
    elif not section.islower():
        bb.warn("%s: SECTION should only use lower case" % pkgname)
}
