INFERANALYZE= /Users/ruibrito/Development/infer-osx-v0.1.0/infer/infer/bin/InferAnalyze $(INFER_OPTIONS) -results_dir '/Users/ruibrito/IdeaProjects/SvgNaviMap/AndroidSvgNaviMap/infer-out'

OBJECTS=cl1 cl2 cl3 cl4 cl5 cl6 cl7 cl8 cl9 cl10 cl11 cl12 cl13 cl14 cl15 cl16 cl17 cl18 cl19 cl20 cl21 cl22 cl23 cl24 cl25 cl26 cl27 cl28 cl29 cl30 cl31 cl32 cl33 cl34 cl35 

default: test

all: test

test: $(OBJECTS)
	echo "Analysis done"

cl1 : 
	$(INFERANALYZE) -cluster xcl1.cluster >cl1

cl2 : 
	$(INFERANALYZE) -cluster xcl2.cluster >cl2

cl3 : 
	$(INFERANALYZE) -cluster xcl3.cluster >cl3

cl4 : cl3
	$(INFERANALYZE) -cluster xcl4.cluster >cl4

cl5 : 
	$(INFERANALYZE) -cluster xcl5.cluster >cl5

cl6 : 
	$(INFERANALYZE) -cluster xcl6.cluster >cl6

cl7 : 
	$(INFERANALYZE) -cluster xcl7.cluster >cl7

cl8 : cl5
	$(INFERANALYZE) -cluster xcl8.cluster >cl8

cl9 : 
	$(INFERANALYZE) -cluster xcl9.cluster >cl9

cl10 : 
	$(INFERANALYZE) -cluster xcl10.cluster >cl10

cl11 : 
	$(INFERANALYZE) -cluster xcl11.cluster >cl11

cl12 : 
	$(INFERANALYZE) -cluster xcl12.cluster >cl12

cl13 : 
	$(INFERANALYZE) -cluster xcl13.cluster >cl13

cl14 : 
	$(INFERANALYZE) -cluster xcl14.cluster >cl14

cl15 : 
	$(INFERANALYZE) -cluster xcl15.cluster >cl15

cl16 : cl11 cl15
	$(INFERANALYZE) -cluster xcl16.cluster >cl16

cl17 : cl11 cl12 cl14 cl16
	$(INFERANALYZE) -cluster xcl17.cluster >cl17

cl18 : 
	$(INFERANALYZE) -cluster xcl18.cluster >cl18

cl19 : cl11 cl17 cl18
	$(INFERANALYZE) -cluster xcl19.cluster >cl19

cl20 : 
	$(INFERANALYZE) -cluster xcl20.cluster >cl20

cl21 : 
	$(INFERANALYZE) -cluster xcl21.cluster >cl21

cl22 : 
	$(INFERANALYZE) -cluster xcl22.cluster >cl22

cl23 : 
	$(INFERANALYZE) -cluster xcl23.cluster >cl23

cl24 : 
	$(INFERANALYZE) -cluster xcl24.cluster >cl24

cl25 : cl21 cl23
	$(INFERANALYZE) -cluster xcl25.cluster >cl25

cl26 : 
	$(INFERANALYZE) -cluster xcl26.cluster >cl26

cl27 : cl6 cl23 cl26
	$(INFERANALYZE) -cluster xcl27.cluster >cl27

cl28 : cl23 cl24 cl25 cl26 cl27
	$(INFERANALYZE) -cluster xcl28.cluster >cl28

cl29 : cl6 cl9 cl21 cl22 cl28
	$(INFERANALYZE) -cluster xcl29.cluster >cl29

cl30 : cl6 cl25 cl29
	$(INFERANALYZE) -cluster xcl30.cluster >cl30

cl31 : 
	$(INFERANALYZE) -cluster xcl31.cluster >cl31

cl32 : 
	$(INFERANALYZE) -cluster xcl32.cluster >cl32

cl33 : cl8 cl9 cl12 cl13 cl14 cl15 cl16 cl17 cl19 cl20 cl27 cl29 cl31 cl32
	$(INFERANALYZE) -cluster xcl33.cluster >cl33

cl34 : cl3 cl4 cl7 cl11 cl29 cl30 cl33
	$(INFERANALYZE) -cluster xcl34.cluster >cl34

cl35 : 
	$(INFERANALYZE) -cluster xcl35.cluster >cl35


clean:
	rm -f $(OBJECTS)
