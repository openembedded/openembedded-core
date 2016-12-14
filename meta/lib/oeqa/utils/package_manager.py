def get_package_manager(d, root_path):
    """
    Returns an OE package manager that can install packages in root_path.
    """
    from oe.package_manager import RpmPM, OpkgPM, DpkgPM

    pkg_class = d.getVar("IMAGE_PKGTYPE")
    if pkg_class == "rpm":
        pm = RpmPM(d,
                   root_path,
                   d.getVar('TARGET_VENDOR'))
        pm.create_configs()

    elif pkg_class == "ipk":
        pm = OpkgPM(d,
                    root_path,
                    d.getVar("IPKGCONF_TARGET"),
                    d.getVar("ALL_MULTILIB_PACKAGE_ARCHS"))

    elif pkg_class == "deb":
        pm = DpkgPM(d,
                    root_path,
                    d.getVar('PACKAGE_ARCHS'),
                    d.getVar('DPKG_ARCH'))

    pm.write_index()
    pm.update()

    return pm
