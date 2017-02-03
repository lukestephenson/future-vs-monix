name := "monix-examples"

version := "1.0"

scalaVersion := "2.12.1"

val monixVersion = "2.2.1"

libraryDependencies ++= Seq(
  "io.monix"                        %% "monix"                       % monixVersion,
  "io.monix"                        %% "monix-cats"                  % monixVersion
)
