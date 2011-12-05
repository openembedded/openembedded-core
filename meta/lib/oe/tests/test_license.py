import unittest
import oe.license

class SeenVisitor(oe.license.LicenseVisitor):
    def __init__(self):
        self.seen = []
        oe.license.LicenseVisitor.__init__(self)

    def visit_Str(self, node):
        self.seen.append(node.s)

class TestSingleLicense(unittest.TestCase):
    licenses = [
        "GPLv2",
        "LGPL-2.0",
        "Artistic",
        "MIT",
        "GPLv3+",
        "FOO_BAR",
    ]
    invalid_licenses = ["GPL/BSD"]

    @staticmethod
    def parse(licensestr):
        visitor = SeenVisitor()
        visitor.visit_string(licensestr)
        return visitor.seen

    def test_single_licenses(self):
        for license in self.licenses:
            licenses = self.parse(license)
            self.assertListEqual(licenses, [license])

    def test_invalid_licenses(self):
        for license in self.invalid_licenses:
            with self.assertRaises(oe.license.InvalidLicense) as cm:
                self.parse(license)
            self.assertEqual(cm.exception.license, license)
