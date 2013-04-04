import unittest
import bb, oe.utils

class TestPackagesFilterOutSystem(unittest.TestCase):
    def test_filter(self):
        """
        Test that oe.utils.packages_filter_out_system works.
        """

        d = bb.data_smart.DataSmart()
        d.setVar("PN", "foo")

        d.setVar("PACKAGES", "foo foo-doc foo-dev")
        pkgs = oe.utils.packages_filter_out_system(d)
        self.assertEqual(pkgs, [])

        d.setVar("PACKAGES", "foo foo-doc foo-data foo-dev")
        pkgs = oe.utils.packages_filter_out_system(d)
        self.assertEqual(pkgs, ["foo-data"])

        d.setVar("PACKAGES", "foo foo-locale-en-gb")
        pkgs = oe.utils.packages_filter_out_system(d)
        self.assertEqual(pkgs, [])

        d.setVar("PACKAGES", "foo foo-data foo-locale-en-gb")
        pkgs = oe.utils.packages_filter_out_system(d)
        self.assertEqual(pkgs, ["foo-data"])
