package com.github.aruma256.lottweaks;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class VersionComparatorTest {

	@Test
	final void test_isVersionNewerOrTheSame() {
		// If tested and reference versions are the same, return true
		assertTrue(VersionComparator.isVersionNewerOrTheSame("3.3.3", "3.3.3"));
		// If tested is newer than reference, return true
		assertTrue(VersionComparator.isVersionNewerOrTheSame("3.3.3", "2.3.3"));
		assertTrue(VersionComparator.isVersionNewerOrTheSame("3.3.3", "3.2.3"));
		assertTrue(VersionComparator.isVersionNewerOrTheSame("3.3.3", "3.3.2"));
		assertTrue(VersionComparator.isVersionNewerOrTheSame("3.3.3", "2.2.3a")); //NOTE: Some older mods have an alphabet at the end of the version
		// If tested is older than reference, return false
		assertFalse(VersionComparator.isVersionNewerOrTheSame("3.3.3", "4.3.3"));
		assertFalse(VersionComparator.isVersionNewerOrTheSame("3.3.3", "3.4.3"));
		assertFalse(VersionComparator.isVersionNewerOrTheSame("3.3.3", "3.3.4"));
		// Should not raise exceptions even if the reference is manipulated
		assertDoesNotThrow(() -> VersionComparator.isVersionNewerOrTheSame("3.3.3", ""));
		assertDoesNotThrow(() -> VersionComparator.isVersionNewerOrTheSame("3.3.3", "ZZZ"));
		assertDoesNotThrow(() -> VersionComparator.isVersionNewerOrTheSame("3.3.3", "Z.Z."));
		assertDoesNotThrow(() -> VersionComparator.isVersionNewerOrTheSame("3.3.3", "0.0.0.0"));
	}

}
